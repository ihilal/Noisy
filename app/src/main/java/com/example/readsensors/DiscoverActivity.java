package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DiscoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String[] topics = intent.getStringArrayExtra("topics");


        // Capture the layout's listView and set the string array as its topics
        ListView listview = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> displayTopics = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                topics);
        listview.setAdapter(displayTopics);

        //make list clickable
//        listview.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> l,View v, int position, long id)
            {
                Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                n.putExtra("position", position);
                n.putExtra("id", id);
                startActivity(n);
            }
        });
    }


}
