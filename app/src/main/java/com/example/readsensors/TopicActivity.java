package com.example.readsensors;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TopicActivity extends AppCompatActivity {
    TextView tvRead;

    String topicPath;
    String topicName;
    int topicCt;
    PubsubAndroid client;
    boolean subsribed = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Get the Intent that started this activity
        Intent intent = getIntent();

        topicName = intent.getStringExtra("topic-string");
        topicPath = intent.getStringExtra("topic-path");
        topicCt =  intent.getIntExtra("topic-ct",0);
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        client = bundle.getParcelable("pubsub_client");

            setContentView(R.layout.activity_topic_ct0);
            // Capture the layout's TextView and set the string as its text
            TextView tvTopicString = findViewById(R.id.tvTopicString);
            tvTopicString.setText(topicName + "\n"+ topicPath +"\n"+Converter.getCTString(topicCt));
            tvRead = findViewById(R.id.tvRead);

        //expert mode

        if(((DataArraySub) TopicActivity.this.getApplication()).getMode()) {
            findViewById(R.id.button3).setVisibility(View.VISIBLE);
            findViewById(R.id.button5).setVisibility(View.VISIBLE);
        }

        else{
            findViewById(R.id.button3).setVisibility(View.INVISIBLE);
            findViewById(R.id.button5).setVisibility(View.INVISIBLE);
        }

        Toast toast = Toast.makeText(TopicActivity.this, topicPath, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void read(View v) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        TextView tv = findViewById(R.id.data);
        tv.setText("Data:");

        String read_res = client.read(topicPath).getResponseText();

        tvRead.setText(currentDateandTime+": "+read_res);
    }


    public void publish(View v) {
        Intent intent = new Intent(this, PublishActivity.class);
        intent.putExtra("topic-path", topicPath);
        intent.putExtra("pubsub_client", client);
        intent.putExtra("topic-string", topicName);
        intent.putExtra("topic-ct", topicCt);
        startActivity(intent);
        finish();
    }

    public void subscribe(View v) {

        Intent intent = new Intent(this, SubscribeActivity.class);
        intent.putExtra("topic-path", topicPath);

        ArrayList<String> index = ((DataArraySub) TopicActivity.this.getApplication()).getIndex();

        if(!index.contains(topicPath)) {

            ((DataArraySub) TopicActivity.this.getApplication()).addDataArray(topicPath);

            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            CoapHandler listen = new CoapHandler() {
                @Override
                public void onLoad(CoapResponse response) {

                    if (!response.getResponseText().equals("")) {
                        String time = sdf.format(Calendar.getInstance().getTime());
                        ((DataArraySub) TopicActivity.this.getApplication()).setDataArray(time + ":   " + response.getResponseText(), topicPath);
                    }
                }

                @Override
                public void onError() {

                    Toast toast = Toast.makeText(TopicActivity.this, "ERROR", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }
            };

            PubsubAndroid.Subscription new_sub = client.new Subscription(listen, topicPath);
            new_sub.subscribe();

            ((DataArraySub) TopicActivity.this.getApplication()).addPubSub(new_sub);

            Toast toast = Toast.makeText(TopicActivity.this, "SUBSCRIBED", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else {
            Toast toast = Toast.makeText(TopicActivity.this, "ALREADY SUBSCRIBED", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

            startActivity(intent);


    }


    public void remove(View v) {
        String remove_res = null;
        remove_res = client.remove(topicPath).getResponseText();

        Toast toast = Toast.makeText(TopicActivity.this, remove_res, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(this, DiscoverActivity.class));
        finish();
    }
}
