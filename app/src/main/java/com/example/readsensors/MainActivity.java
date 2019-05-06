package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

//    public static final String EXTRA_MESSAGE = "com.example.readsensors.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void discover(View v){
        Intent intent = new Intent(this, DiscoverActivity.class);
        EditText ipAddress = (EditText) findViewById(R.id.editText5);
        String message = ipAddress.getText().toString();
        String[] topics = PubSub.discover(message, 5683, 5000,".well-known/core");
        intent.putExtra("topics", topics);
        startActivity(intent);
    }

// ip 127.0.0.1 5683
    //ip 192.16.125.232
}
