package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.californium.core.coap.CoAP;

public class CreateTopicActivity extends AppCompatActivity {

    String path ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic_activity);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        path = intent.getStringExtra("path");


    }

    public void create(View v){
        EditText textview = (EditText) findViewById(R.id.editText3);
        String name = textview.getText().toString();
        EditText textview1 = (EditText) findViewById(R.id.editText4);
        int ct = Integer.parseInt(textview1.getText().toString());

        Topic topic = new Topic(name, ct);

        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String ip = prefs.getString("address", "");
        CoAP.ResponseCode  response = PubSub.create(ip,5683, path, topic );

        Toast toast = Toast.makeText(CreateTopicActivity.this, path , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(CreateTopicActivity.this, DiscoverActivity.class));
    }
}
