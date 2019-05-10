package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        EditText ipAddress = (EditText) findViewById(R.id.etAddress);
        ipAddress.setText("130.229.158.86");
    }

    public void discover(View v){
        Intent intent = new Intent(this, DiscoverActivity.class);
        EditText ipAddress = (EditText) findViewById(R.id.etAddress);
        String address = ipAddress.getText().toString();

        //save data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("address", address);
        editor.commit();

//        String[] topics = PubSub.discover(address, 5683, 5000,".well-known/core");
//        intent.putExtra("topics", topics);
        startActivity(intent);
    }

}
