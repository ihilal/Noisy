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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DiscoverActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_topics);


        listview = findViewById(R.id.sublist);

        dataArray = ((DataArraySub) SubscribedTopics.this.getApplication()).getPaths();

        final ArrayAdapter<String> displayData = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataArray);
        listview.setAdapter(displayData);

        //make list clickable
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Intent n = new Intent(getApplicationContext(), SubscribeActivity.class);
                n.putExtra("topic-path", dataArray.get(position));
                startActivity(n);
            }
        });
    }

}
