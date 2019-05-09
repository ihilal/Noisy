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
    TextView tvRead;
    SharedPreferences prefs;
    String content, address;

    Topic topic = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        tvRead = findViewById(R.id.tvRead);
        // Get the Intent that started this activity
        Intent intent = getIntent();
        String stringTopic = intent.getStringExtra("topic-string");
//        String pos = intent.getStringExtra("position");
//        String id = intent.getStringExtra("id");
        topic = new Topic(stringTopic);
        // Capture the layout's TextView and set the string as its text
        TextView tvTopicString = findViewById(R.id.tvTopicString);
        tvTopicString.setText(stringTopic);

        //display content read
        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");
        content = PubSub.read(address, 5683, topic);
        tvRead.setText(content);
    }

    public void read(View v) {
        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");
        content = PubSub.read(address, 5683, topic);
        tvRead.setText(content);
    }


    public void publish(View v){
        Intent intent = new Intent(this, PublishActivity.class);
//        Button publish = (Button) findViewById(R.id.button3);
        intent.putExtra("topic-string", topic.toString());
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
        intent.putExtra("path", topic.getPath());
        startActivity(intent);
    }

    public void remove(View v){
        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String address = prefs.getString("address", "");

        CoAP.ResponseCode response = PubSub.remove(address, 5683, topic);

        Toast toast = Toast.makeText(TopicActivity.this, topic.getPathAsString() , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(this, DiscoverActivity.class));
    }
}
