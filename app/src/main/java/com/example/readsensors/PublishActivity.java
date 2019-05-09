package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.coap.CoAP;

public class PublishActivity extends AppCompatActivity {


    String path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        path = intent.getStringExtra("topic-string");

    }

    public void publish(View v){
        EditText textview = (EditText) findViewById(R.id.editText2);
        String content = textview.getText().toString();
        Topic topic = new Topic(path);

        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String ip = prefs.getString("address", "");
        CoAP.ResponseCode response = PubSub.publish(ip,5683, topic, content);

        Toast toast = Toast.makeText(PublishActivity.this, response.toString() , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        textview.setText("");
    }
}
