package com.example.readsensors;

public class SubscribeListener {
    private String data = null;
    private ChangeListener listener;

    public void setData(String data){
        this.data = data;
        if (listener != null) listener.onChange();
    }

    public String getData(){
         return this.data;
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
