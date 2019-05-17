package com.example.readsensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TopicActivity extends AppCompatActivity {
    TextView tvRead;

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

        stringTopic = intent.getStringExtra("topic-string");

        path = intent.getStringExtra("topic-path");
        Bundle bundle = intent.getExtras();
        client = bundle.getParcelable("pubsub_client");



        topic = new Topic(new WebLink(stringTopic));


        if (topic.getCt() == 0) {
            setContentView(R.layout.activity_topic_ct0);
            // Capture the layout's TextView and set the string as its text
            TextView tvTopicString = findViewById(R.id.tvTopicString);
            tvTopicString.setText(topic.getPath() + "   ;   " + MediaTypeRegistry.toString(topic.getCt()));
            tvRead = findViewById(R.id.tvRead);

        } else if (topic.getCt() == 40) {
            setContentView(R.layout.activity_topic_ct40);
            TextView tvTopicString = findViewById(R.id.tvTopicString2);
            tvTopicString.setText(topic.getPath() + "   ;   " + MediaTypeRegistry.toString(topic.getCt()));
        }

        Button btnParent = findViewById(R.id.btnParent);
        Toast toast = Toast.makeText(TopicActivity.this, path, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        if(path.equals("ps/") || path.split("/").length == 2)
            btnParent.setVisibility(View.GONE);
    }

    public void viewParent(View v) {
        Intent intent = new Intent(getApplicationContext(), TopicActivity.class);
        String pathWithoutEndSlash = path.substring(0,path.lastIndexOf('/'));
        String parentPath = pathWithoutEndSlash.substring(0,pathWithoutEndSlash.lastIndexOf('/'));

        WebLink parentTopic = new WebLink(parentPath);
        parentTopic.getAttributes().setAttribute("ct", "40");
        intent.putExtra("topic-string", parentTopic.toString());
        intent.putExtra("topic-path", parentPath);
        intent.putExtra("pubsub_client",client);
        startActivity(intent);
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


    public void publish(View v) {
        Intent intent = new Intent(this, PublishActivity.class);
        intent.putExtra("topic-path", path);
        intent.putExtra("pubsub_client", client);
        intent.putExtra("topic-string", stringTopic);
        startActivity(intent);
        finish();
    }

    public void subscribe(View v) {


        ((DataArraySub) TopicActivity.this.getApplication()).addDataArray(path);

        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");


        SubscribeListener listen = new SubscribeListener() {
            @Override
            public void onResponse(String responseText) {
                String time = sdf.format(Calendar.getInstance().getTime());
                ((DataArraySub) TopicActivity.this.getApplication()).setDataArray(time + ":   " + responseText, path);
            }

            @Override
            public void onError() {


                Toast toast = Toast.makeText(TopicActivity.this, "ERROR", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
        };

        PubsubAndroid.Subscription new_sub = client.new Subscription(path, listen);
        new_sub.subscribe();

        ((DataArraySub) TopicActivity.this.getApplication()).addPubSub(new_sub);

        Toast toast = Toast.makeText(TopicActivity.this, "SUBSCRIBED", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();


    }

    public void createSubTopic(View v) {
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra("topic-path", path);
        intent.putExtra("pubsub_client", client);
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
