package com.example.readsensors;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

public class PubSub {

    public static String[] discover(String host, int port, long timeout){
        String[] topics = {"</ps/topic1>;ct=40", "</ps/topic2>;ct=40", "</ps/topic3>;ct=40"};
        return topics;
    }
}
