package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SubscribeActivity extends AppCompatActivity {
    TextView tvContentSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String path = intent.getStringExtra("topic-path");

        tvContentSubscribe = findViewById(R.id.contentSubscribe);
        PubSub.subscribe();
    }


}
