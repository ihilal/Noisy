package com.example.readsensors;

import android.app.Application;
import android.databinding.ObservableArrayList;

import java.util.ArrayList;

public class DataArraySub extends Application {
    private ArrayList<PubsubAndroid.Subscription> pubsubs = new ArrayList<>();

    private SubscribeListener l;

    private ArrayList<String> index = new ArrayList<String>() ;

    private ArrayList<ObservableArrayList<String>> dataArray= new ArrayList<ObservableArrayList<String>>();

    public ObservableArrayList<String> getData(String name) {
        int i = index.indexOf(name);
        return dataArray.get(i);
    }

    public void addPubSub(PubsubAndroid.Subscription ps){
        pubsubs.add(ps);
    }

    public PubsubAndroid.Subscription getPubSub(String name){
        int i = index.indexOf(name);
        return pubsubs.get(i);
    }

    public void setDataArray(String s, String name){
        int i = index.indexOf(name);
        dataArray.get(i).add(s);
        if(!l.equals(null))
         l.onResponse(s);
    }

    public void addDataArray(String name){
        index.add(name);
        dataArray.add(new ObservableArrayList<String>());
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
