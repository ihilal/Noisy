package com.example.readsensors;

import android.os.Parcel;
import android.os.Parcelable;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.core.network.RandomTokenGenerator;
import org.eclipse.californium.core.network.config.NetworkConfig;
import java.io.IOException;
import java.util.Set;


public class PubSub {

    private String host;
    private  int port  = 5683;
    private static final String scheme = "coap";
    private long timeout;
    private NetworkConfig config = NetworkConfig.createStandardWithoutFile();




    public PubSub(String host , int port , long timeout ){
        this.host = host ;
        this.port = port ;
        this.timeout = timeout;
    }

    public PubSub() {
    }

    public NetworkConfig getConfig() {
        return config;
    }

    public void setConfig(NetworkConfig config) {
        this.config = config;
    }

    public int getPort() {
        return this.port;
    }

    public String getHost() {
        return this.host;
    }


    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    /* Returns array of Topic objects and Confirmation Code*/

    public Set<WebLink> discover() throws  IOException {
        CoapClient client = new CoapClient(scheme, this.getHost(), this.getPort());
        client.setTimeout(this.timeout);

        Set<WebLink> x = null;
        try {

            x = client.discover("?rt=core.ps");
        } catch (RuntimeException e) {

            System.err.println(" THERE IS NO CoAP BROKER FOUND");
        }

        if (x == null) {
            throw new IOException(" NO RESPONSE , TIMEOUT ");
        } else if (x.size() == 0) {

            System.out.println(" THE CONTENT FORMAT IS NOT 40 = APPLICATION LINK FORMAT");
            return x;
        }

        return x;
    }

    public Set<WebLink> discover(String query) throws RuntimeException, IOException {

        Request discover = Request.newGet();
        discover.getOptions().setUriPath(".well-known/core?" + query);

        CoapClient client = new CoapClient(scheme, this.getHost(), this.getPort());
        client.setTimeout(this.timeout);

        CoapResponse response = null;
        try {
            response = client.advanced(discover);
        } catch (RuntimeException e) {

            System.err.println(" THERE IS NO CoAP BROKER FOUND");
        }


        if (response == null) {

            throw  new IOException(" NO RESPONSE , TIMEOUT");

        }
        return LinkFormat.parse(response.getResponseText());

    }

    /* Returns topic and Confirmation Code */
    public String create(String path , String name , int ct) throws  IOException {


        CoapClient client = new CoapClient(scheme, this.getHost(), this.getPort(), path);
        client.setTimeout(this.timeout);


        StringBuilder sb = new StringBuilder().append("<").append(name).append(">;ct=").append(ct);
        String payload = sb.toString();


        Request req = Request.newPost();
        req.setPayload(payload);
        req.getOptions().setContentFormat(ct);

        CoapResponse res = null;
        try {
            res = client.advanced(req);
        } catch (RuntimeException e) {

            System.err.println(" THERE IS NO CoAP BROKER FOUND");
        }


        if (res == null) {

            throw new IOException("INVALID PATH ");

        }

        return res.getResponseText() + "\n" + res.getOptions().toString();


    }

    /* Returns Confirmation Code */
    public String publish( String path, String payload , int ct ) throws  IOException {


        CoapClient client = new CoapClient(scheme, this.getHost(), this.getPort(), path);
        client.setTimeout(this.timeout);

        CoapResponse res = null;
        try {
            res = client.put(payload,ct);

        } catch (RuntimeException e) {

            System.err.println(" THERE IS NO CoAP BROKER FOUND");

        }


        if (res == null) {

            throw new IOException(" INVALID PATH ");

        }

        return res.getCode().toString() + " " + res.getCode().name();

    }

    /* Returns Content and Confirmation Code */
    public String read(String path) throws  IOException {
        CoapClient client = new CoapClient(scheme, this.getHost(), this.getPort(), path);
        client.setTimeout(this.timeout);


        CoapResponse x = null;
        try {
            x = client.get();

        } catch (RuntimeException e) {

            System.err.println(" THERE IS NO CoAP BROKER FOUND");


        }

        if (x == null) {

            throw new IOException(" PATH IS NOT VALID");
        }


        return x.getResponseText();

    }

    /* Returns Confirmation Code */
    public String remove(String path) throws  IOException {

        CoapClient client = new CoapClient(scheme, this.getHost(), this.getPort(), path);
        client.setTimeout(this.timeout);

        CoapResponse response = null;
        try {
            response = client.delete();

        } catch (RuntimeException e) {

            System.err.println(" THERE IS NO CoAP BROKER FOUND");

        }


        if (response == null) {
            throw new IOException();
        }

        return response.getCode().toString() + " " + response.getCode().name();
    }


    public Topic[] get_Topics(Set<WebLink> links){

        int num = links.size();

        Topic [] result = new Topic[num];


        int i = 0;

        for (WebLink x:links) {

            result[i] = new Topic(x);
            i++;

        }
        return  result;
    }



    public class Subscription {
        private CoapClient client;
        private CoapObserveRelation relation;
        private String path;
        private SubscribeListener listener;

        public Subscription(String path, SubscribeListener listener) {
            this.path = path;
            this.listener = listener;
            this.relation = null;
            this.client = null;
        }

        public void subscribe() {

            Request req = new Request(CoAP.Code.GET);

            client = new CoapClient(scheme, getHost(), getPort(), path);
            client.useExecutor();
            client.setTimeout(timeout);

            req.setURI(client.getURI());
            req.setObserve();

            config.set(NetworkConfig.Keys.TOKEN_SIZE_LIMIT,4);
            RandomTokenGenerator rand = new RandomTokenGenerator(config);
            Token token = rand.createToken(false);
            req.setToken(token);

            CoapHandler handler = new CoapHandler() {
                @Override
                public void onLoad(CoapResponse coapResponse) {
                    listener.onResponse(coapResponse.getResponseText());
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            };


            try{
                relation = client.observe(req ,handler);
            }
            catch (RuntimeException e ){

                System.err.println(" THERE IS NO CoAP BROKER FOUND");
            }

            return;
        }

        public void unsubscribe() {
            if (this.relation != null) {
                relation.proactiveCancel();
                int mid = relation.getCurrent().advanced().getMID();
                while (relation.getCurrent().advanced().getMID() == mid) ;
            }
            if (this.client != null)
                client.shutdown();
        }
    }

}
