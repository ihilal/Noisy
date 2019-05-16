package com.example.readsensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.IOException;

public class TopicActivity extends AppCompatActivity {
    TextView tvRead;
    SharedPreferences prefs;
    String content, address;

    String path;
    String stringTopic;
    Topic topic = null;
    PubsubAndroid client;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the Intent that started this activity
        Intent intent = getIntent();
//        if (intent.hasExtra("topic-string")) {
            stringTopic = intent.getStringExtra("topic-string");
//        }
        path = intent.getStringExtra("topic-path");
        Bundle bundle = intent.getExtras();
        client = bundle.getParcelable("pubsub_client");


        topic = new Topic(new WebLink(stringTopic));


        if(topic.getCt() == 0){
            setContentView(R.layout.activity_topic_ct0);
            // Capture the layout's TextView and set the string as its text
            TextView tvTopicString = findViewById(R.id.tvTopicString);
            tvTopicString.setText(topic.getPath() + "   ;   " + MediaTypeRegistry.toString(topic.getCt()));
            tvRead = findViewById(R.id.tvRead);
            //display content read
//            prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
//            address = prefs.getString("address", "");
//            res = PubSub.read(address, 5683, topic);
//            content = res.getResponseText();
//            tvRead.setText(content);
        }else if(topic.getCt() == 40){
            setContentView(R.layout.activity_topic_ct40);
            TextView tvTopicString = findViewById(R.id.tvTopicString2);
            tvTopicString.setText(topic.getPath() + "   ;   " + MediaTypeRegistry.toString(topic.getCt()));
        }

    }

    public void read(View v) {

        String read_res = null;
        try {
            read_res = client.read(path);
        } catch (IOException e) {

            Toast toast = Toast.makeText(TopicActivity.this, "WRONG PATH OR TIMEOUT ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        tvRead.setText(read_res);
    }


    public void publish(View v){
        Intent intent = new Intent(this, PublishActivity.class);
        intent.putExtra("topic-path", path);
        intent.putExtra("pubsub_client",client);
        intent.putExtra("topic-string", stringTopic);
        startActivity(intent);
        finish();
    }

    public void subscribe(View v){
        Intent intent = new Intent(this, SubscribeActivity.class);
        intent.putExtra("topic-path", path);
        intent.putExtra("pubsub_client",client);
        startActivity(intent);
    }

    public void createSubTopic(View v){
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra("topic-path", path);
        intent.putExtra("pubsub_client",client);
        startActivity(intent);
    }

    public void remove(View v) {

        String remove_res = null;
        try {
            remove_res = client.remove(path);
        } catch (IOException e) {
            Toast toast = Toast.makeText(TopicActivity.this, "WRONG PATH OR TIMEOUT ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

        Toast toast = Toast.makeText(TopicActivity.this, remove_res, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(this, DiscoverActivity.class));
        finish();
    }
}
