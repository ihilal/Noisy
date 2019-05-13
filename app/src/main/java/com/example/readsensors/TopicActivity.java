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

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String stringTopic = intent.getStringExtra("topic-string");
//        String pos = intent.getStringExtra("position");
//        String id = intent.getStringExtra("id");
        topic = new Topic(stringTopic);

        if(topic.getCt() == 0){
            setContentView(R.layout.activity_topic_ct0);
            // Capture the layout's TextView and set the string as its text
            TextView tvTopicString = findViewById(R.id.tvTopicString);
            tvTopicString.setText(stringTopic);
            tvRead = findViewById(R.id.tvRead);
            //display content read
            prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
            address = prefs.getString("address", "");
            content = PubSub.read(address, 5683, topic);
            tvRead.setText(content);
        }else if(topic.getCt() == 40){
            setContentView(R.layout.activity_topic_ct40);
            TextView tvTopicString = findViewById(R.id.tvTopicString2);
            tvTopicString.setText(stringTopic);
        }

    }

    public void read(View v) {
        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");
        content = PubSub.read(address, 5683, topic);
        tvRead.setText(content);
    }


    public void publish(View v){
        Intent intent = new Intent(this, PublishActivity.class);
        intent.putExtra("topic-string", topic.toString());
        startActivity(intent);
    }

    public void subscribe(View v){
        Intent intent = new Intent(this, SubscribeActivity.class);
        intent.putExtra("topic-path", topic.getPathString());
        startActivity(intent);
    }

    public void createSubTopic(View v){
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra("topic-string", topic.toString());
        startActivity(intent);
    }

    public void remove(View v){
        //load data
        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");

        CoAP.ResponseCode response = PubSub.remove(address, 5683, topic);

        Toast toast = Toast.makeText(TopicActivity.this, response.toString() , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(this, DiscoverActivity.class));
    }
}
