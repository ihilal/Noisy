package com.example.readsensors;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class DataArraySub extends Application {

    private ArrayList<String> index = new ArrayList<String>() ;

    private ArrayList<ArrayList<String>> dataArray= new ArrayList<ArrayList<String>>();

    public ArrayList<String> getData(String name) {
        int i = index.indexOf(name);
        return dataArray.get(i);
    }

    public void setDataArray(String s, String name){
        int i = index.indexOf(name);
        dataArray.get(i).add(s);
    }

    public void addDataArray(String name){
        index.add(name);
        dataArray.add(new ArrayList<String>());
    }

    public void removeDataArray(String name){
        int i = index.indexOf(name);
        index.remove(i);
        dataArray.remove(i);
    }
}
