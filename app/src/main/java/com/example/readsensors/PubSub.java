package com.example.readsensors;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

public class PubSub {

    public static String[] discover(String host, int port, long timeout, String path){
        CoapClient client = new CoapClient("coap", host, port,path);
        client.setTimeout(timeout);
        String content = client.get().getResponseText();
        String[] topics = content.split(",");
        for(int i = 0; i < topics.length; i++) {
            System.out.println("FOUND TOPIC " + (i+1) + ":      " + topics[i]);
        }
        return topics;
    }

    public static String create(String host, int port, long timeout, String path, String name, int ct){
        CoapClient client = new CoapClient("coap", host, port,path);
        StringBuilder sb = new StringBuilder().append("<").append(name).append(">;ct=").append(ct);
        String payload = sb.toString();
        CoapResponse resp = client.post(payload,0);
        String result = resp.getResponseText();
        return result;
    }

    public static String publish(String host, int port, String path, String payload, int ct){
        CoapClient client = new CoapClient("coap", host, port,path);
        CoapResponse resp =         client.put(payload,ct);
        String result = resp.getResponseText();

        return  result;
    }

    public static void read(String host, int port, String path){
        CoapClient client = new CoapClient("coap", host, port,path);
        String data = client.get().getResponseText();
        System.out.println(data);
    }

    public static void remove(String host, int port, String path){
        CoapClient client = new CoapClient("coap", host, port,path);
        client.delete();
    }


    public static String get_path(String path ){

        char [] arr = path.toCharArray();
        String result="";

        for(int i =2 ; i < arr.length; i++){

            if (arr[i]!= '>'){

                result = result + arr[i];

            }else{
                return result;

           }


        }

        return  result;
    }


}
