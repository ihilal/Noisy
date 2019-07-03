package com.example.readsensors;

import android.app.Application;

import java.util.ArrayList;

public class DataArraySub extends Application {

   // private boolean expertMode = false;

    private ArrayList<PubsubAndroid.Subscription> pubsubs = new ArrayList<>();

    private SubscribeListener l;

    private ArrayList<String> index = new ArrayList<String>() ;

    private ArrayList<ArrayList<String>> dataArray= new ArrayList<ArrayList<String>>();

    public ArrayList<String> getData(String name) {
        int i = index.indexOf(name);
        return dataArray.get(i);
    }
    public ArrayList<String> getIndex(){
        return index;
    }

    public void addPubSub(PubsubAndroid.Subscription ps){
        pubsubs.add(ps);
    }

    public PubsubAndroid.Subscription getPubSub(String name){
        int i = index.indexOf(name);
        return pubsubs.get(i);
    }

   /* public void toggleExpertMode(){
        if (expertMode)
            expertMode = false;
        else
            expertMode = true;
    }

    public boolean getMode(){
        return expertMode;
    }*/

    public void setDataArray(String s, String name){
        int i = index.indexOf(name);
        dataArray.get(i).add(s);
        if(!l.equals(null))
         l.onResponse(s);
    }

    public void addDataArray(String name){
        index.add(name);
        dataArray.add(new ArrayList<String>());
    }

    public void removeDataArray(String name){
        int i = index.indexOf(name);
        index.remove(i);
        dataArray.remove(i);
        pubsubs.remove(i);
    }

    public void setListener(SubscribeListener ls){
        this.l = ls;
    }

    public ArrayList<String> getPaths(){
        return index;
    }
}
