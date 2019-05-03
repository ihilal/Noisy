package com.example.readsensors;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

public class PubSub {

    public static String discover(String host, int port, long timeout){
        String content = "";
        CoapClient client = new CoapClient("coap", host, port,"");
        client.setTimeout(timeout);
        CoapResponse response = client.get();
        if (response.isSuccess())
            content = response.getResponseText();

        return content;
    }
}
