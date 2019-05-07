package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DiscoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
//        String[] topics = intent.getStringArrayExtra("topics");

        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", "");
        String[] topics = PubSub.discover(ip, 5683, 5000,".well-known/core");


        // Capture the layout's listView and set the string array as its topics
        ListView listview = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> displayTopics = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                topics);
        listview.setAdapter(displayTopics);

        //make list clickable
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> l,View v, int position, long id)
            {
                Intent n = new Intent(getApplicationContext(), TopicActivity.class);
//                String pos = String.valueOf(position);
                n.putExtra("name", l.getItemAtPosition(position).toString());
                n.putExtra("position", String.valueOf(position));
                n.putExtra("id", String.valueOf(id));
                startActivity(n);
            }
        });

    }

    public void createMainTopic(View v){
        Intent intent = new Intent(this, CreateMainTopicActivity.class);
        Button createNewTopic = (Button) findViewById(R.id.button);
        startActivity(intent);
    }



}
