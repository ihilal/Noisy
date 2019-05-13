package com.example.readsensors;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;


public class PubSub {


    /* Returns array of Topic objects */
    public static Topic[] discover(String host, int port, long timeout, String path) {
        CoapClient client = new CoapClient("coap", host, port, path);
        client.setTimeout(timeout);
        String content = client.get().getResponseText();
        String[] topicS = content.split(",");
        Topic[] topicT = new Topic[topicS.length];
        for (int i = 0; i < topicS.length; i++) {
            topicT[i] = new Topic(topicS[i]);
        }
        return topicT;
    }

//    /* Returns Confirmation Code */
//    public static CoAP.ResponseCode create(String host, int port, String path, Topic topic) {
//        CoapClient client = new CoapClient("coap", host, port, path);
//        String payload = topic.makeCreate();
//        CoapResponse resp = client.post(payload, 0);
//
//        return CoAP.ResponseCode.valueOf(resp.getCode().value);
//    }


    /* ------------------------------ */

    public static CoAP.ResponseCode create(String host, int port, Topic topic) {
        CoapClient client = new CoapClient("coap", host, port, "ps");
        String payload = topic.makeCreate();
        CoapResponse resp = client.post(payload, 0);

        topic.setPath();

        return CoAP.ResponseCode.valueOf(resp.getCode().value);
    }

    public static CoAP.ResponseCode create(String host, int port, Topic parent, Topic child) {
        CoapClient client = new CoapClient("coap", host, port, parent.getPath());
        String payload = child.makeCreate();
        CoapResponse resp = client.post(payload, 0);

        child.setPath(parent);

        return CoAP.ResponseCode.valueOf(resp.getCode().value);
    }

    /* ------------------------------ */


    /* Returns Confirmation Code */
    public static CoAP.ResponseCode publish(String host, int port, Topic topic, String payload) {
        CoapClient client = new CoapClient("coap", host, port, topic.getPath());
        CoapResponse resp = client.put(payload, topic.getCt());

        return CoAP.ResponseCode.valueOf(resp.getCode().value);
    }

    /* Returns Content */
    public static String read(String host, int port, Topic topic) {
        CoapClient client = new CoapClient("coap", host, port, topic.getPath());
        String data = client.get().getResponseText();

        return data;
    }

    /* Returns Confirmation Code */
    public static CoAP.ResponseCode remove(String host, int port, Topic topic) {
        CoapClient client = new CoapClient("coap", host, port, topic.getPath());
        CoapResponse resp =client.delete();

        return CoAP.ResponseCode.valueOf(resp.getCode().value);
    }

    /* Gets a stream of Content */
    public static SubscribeListener subscribe(String host, int port, String path) {

        final SubscribeListener listener = new SubscribeListener();
        CoapClient client = new CoapClient("coap", host, port, path);
        Request req = new Request(CoAP.Code.GET);
        req.setURI("coap://"+host+":"+port+"/"+path);
        req.setObserve();
        byte i[] = {0x21};
        Token tt = new Token(i);
        req.setToken(tt);

        CoapHandler handler = new CoapHandler() {
            @Override
            public void onLoad(CoapResponse coapResponse) {
                listener.setData(coapResponse.getResponseText());
            }

            @Override
            public void onError() {
                listener.setData("error");
            }
        };
        client.observe(req, handler);
        return listener;
    }
}