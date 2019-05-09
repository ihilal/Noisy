package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.californium.core.coap.CoAP;

public class CreateTopicActivity extends AppCompatActivity {

    Topic parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic_activity);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String strParent;
        if (intent.hasExtra("topic-string")) {
            strParent = intent.getStringExtra("topic-string");
            Toast toast = Toast.makeText(this, strParent , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            parent = new Topic(strParent);
        }


    }

    public void create(View v){
        EditText etName = (EditText) findViewById(R.id.etName);
        String name = etName.getText().toString();
        EditText etCT = (EditText) findViewById(R.id.etCT);
        int ct = Integer.parseInt(etCT.getText().toString());



        Topic topic = new Topic(name, ct);

        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String address = prefs.getString("address", "");
        CoAP.ResponseCode response;
        if (parent != null)
            response = PubSub.create(address,5683, parent, topic);
        else {
            response = PubSub.create(address, 5683, topic);
        }

        Toast toast = Toast.makeText(CreateTopicActivity.this, response.toString() , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(CreateTopicActivity.this, DiscoverActivity.class));
    }
}
