package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.coap.CoAP;

public class TopicActivity extends AppCompatActivity {

    Topic topic = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
//        String pos = intent.getStringExtra("position");
//        String id = intent.getStringExtra("id");
        topic = new Topic(name);
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(name);
    }

    public void publish(View v){
        Intent intent = new Intent(this, PublishActivity.class);
//        Button publish = (Button) findViewById(R.id.button3);
        intent.putExtra("name", topic.makeURI());
        startActivity(intent);
    }

    public void subscribe(View v){
        Intent intent = new Intent(this, SubscribeActivity.class);
//        Button subscribe = (Button) findViewById(R.id.button4);
        startActivity(intent);
    }

    public void createSubTopic(View v){
        Intent intent = new Intent(this, CreateTopicActivity.class);
//        Button create = (Button) findViewById(R.id.button7);
        intent.putExtra("name", topic.getPath());
        startActivity(intent);
    }

    public void remove(View v){
        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", "");

        CoAP.ResponseCode response = PubSub.remove(ip, 5683, topic);

        Toast toast = Toast.makeText(TopicActivity.this, topic.getPath() , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(this, DiscoverActivity.class));
    }
}
