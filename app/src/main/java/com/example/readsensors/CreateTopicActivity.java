package com.example.readsensors;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CreateTopicActivity extends AppCompatActivity {

    String path = "ps/";

    ArrayList<String> crTopics ;
    ArrayList<String> cruri;
    ArrayList<Integer> crct;

    PubsubAndroid client;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DiscoverActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic_activity);

        // Get the Intent that started this activity


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        client = bundle.getParcelable("pubsub_client");
        crTopics = intent.getStringArrayListExtra("topic-string");
        cruri = intent.getStringArrayListExtra("topic-path");
        crct = intent.getIntegerArrayListExtra("topic-ct");



        // Capture the layout's listView and set the string array as its topics
        ListView listview = (ListView) findViewById(R.id.create_list);
        ArrayAdapter<String> displayTopics = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cruri);
        listview.setAdapter(displayTopics);

        //make list clickable
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {

              if(!path.equals(cruri.get(position))) {
              path = cruri.get(position);
              for(int a = 0; a<l.getChildCount(); a++)
                  l.getChildAt(a).setBackgroundColor(View.INVISIBLE);
              l.getChildAt(position).setBackgroundColor(Color.GRAY);

              Toast toast = Toast.makeText(CreateTopicActivity.this, crTopics.get(position)+"    SELECTED", Toast.LENGTH_SHORT);
              toast.setGravity(Gravity.CENTER, 0, 0);
              toast.show();
            }
              else{
                  path = "ps/";

                  l.getChildAt(position).setBackgroundColor(View.INVISIBLE);

                  Toast toast = Toast.makeText(CreateTopicActivity.this, "ps/ SELECTED", Toast.LENGTH_SHORT);
                  toast.setGravity(Gravity.CENTER, 0, 0);
                  toast.show();
              }
            }
        });

    }

    public void create(View v){
        EditText etName = (EditText) findViewById(R.id.etName);
        String name = etName.getText().toString();
        EditText etCT = (EditText) findViewById(R.id.etCT);
        int ct = Integer.parseInt(etCT.getText().toString());

        String res = null;
        res = client.create(name,ct,path).getResponseText();


        Toast toast = Toast.makeText(CreateTopicActivity.this, path , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(CreateTopicActivity.this, DiscoverActivity.class));

        finish();
    }
}
