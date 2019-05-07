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

public class CreateMainTopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_main_topic);

        // Get the Intent that started this activity
        Intent intent = getIntent();
    }

    public void create(View v){
        EditText textview = (EditText) findViewById(R.id.editText);
        String name = textview.getText().toString();
        EditText textview1 = (EditText) findViewById(R.id.editText6);
        int ct = Integer.parseInt(textview1.getText().toString());

        Topic topic = new Topic(name, ct);

        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", "");

        CoAP.ResponseCode response = PubSub.create(ip,5683, "ps", topic );

        Toast toast = Toast.makeText(CreateMainTopicActivity.this, response.toString() , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(CreateMainTopicActivity.this, DiscoverActivity.class));
    }

}
