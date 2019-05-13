package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SubscribeActivity extends AppCompatActivity {
    TextView tvContentSubscribe;
    SharedPreferences prefs;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);


        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String path = intent.getStringExtra("topic-path");

        tvContentSubscribe = findViewById(R.id.contentSubscribe);

        final SubscribeListener data = PubSub.subscribe(address, 5683, path);

        data.setListener(new SubscribeListener.ChangeListener() {
            @Override
            public void onChange() {
                tvContentSubscribe.setText(data.getData());
            }
        });
    }
}
