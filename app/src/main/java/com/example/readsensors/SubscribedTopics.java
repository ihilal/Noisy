package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SubscribedTopics extends AppCompatActivity {

    ListView listview;
    ArrayList<String> dataArray= new ArrayList<String>();
    PubsubAndroid client;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_topics);

// Get the Intent that started this activity
        Intent intent = getIntent();
        path = intent.getStringExtra("topic-path");
        Bundle bundle = intent.getExtras();
        client = bundle.getParcelable("pubsub_client");

        listview = findViewById(R.id.sublist);

        dataArray.add(path);

        final ArrayAdapter<String> displayData = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataArray);
        listview.setAdapter(displayData);

        //make list clickable
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Intent n = new Intent(getApplicationContext(), SubscribeActivity.class);
                n.putExtra("topic-path", path);
                n.putExtra("pubsub_client",client);
                startActivity(n);
            }
        });
    }

}
