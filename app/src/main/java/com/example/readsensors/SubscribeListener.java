package com.example.readsensors;

public interface SubscribeListener {
    void onResponse(String responseText);

    void onError();
}
