package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.californium.core.WebLink;

import java.io.IOException;
import java.util.Set;

public class DiscoverActivity extends AppCompatActivity {

    SwipeRefreshLayout mySwipeRefreshLayout;
    SharedPreferences prefs;
    String address;
    ListView listview;
    Set<WebLink> topics;
    PubsubAndroid client;
    String query = "";

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        //load data
        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");

        client = new PubsubAndroid(address, 5683, 5000);

        try {

            String broker = client.discover().toString();
            Toast toast = Toast.makeText(this, "BROKER IS RUNNING PS\n" + broker, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            topics = Converter.getAllWebLinks(client.discover(query));
            final String[] stringTopics = new String[topics.size()];
            final String[] stringuri = new String[topics.size()];
            int i = 0;


            for (WebLink x : topics) {
                if (!x.toString().equals("</ps>\n\tct:\t[40]")) {
                    stringTopics[i] = x.toString();
                    stringuri[i] = x.getURI().substring(1) + "/";
                    i++;
                }
            }
            if(stringTopics.length>0) {
                if (stringTopics[0] != null) {

                    // Capture the layout's listView and set the string array as its topics
                    listview = (ListView) findViewById(R.id.list);
                    ArrayAdapter<String> displayTopics = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringTopics);
                    listview.setAdapter(displayTopics);

                    //make list clickable
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                            Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                            n.putExtra("topic-string", stringTopics[position]);
                            n.putExtra("topic-path", stringuri[position]);
                            n.putExtra("pubsub_client", client);
                            startActivity(n);
                        }
                    });

                    mySwipeRefreshLayout = findViewById(R.id.swiperefresh);

                    mySwipeRefreshLayout.setOnRefreshListener(
                            new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    Log.i("log-tag", "onRefresh called from SwipeRefreshLayout");
                                    // This method performs the actual data-refresh operation.
                                    // The method calls setRefreshing(false) when it's finished.
                                    myUpdateOperation();
                                }
                            }
                    );
                }
            }

            } catch(RuntimeException e){
                Toast toast = Toast.makeText(this, "WRONG HOST , TIMEOUT", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                onBackPressed();
            }


    }

    public void createMainTopic(View v) {
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra("pubsub_client", client);
        startActivity(intent);
        finish();
    }

    public void filter(View v) {
        EditText etQuery = (EditText) findViewById(R.id.etQuery);
        query = etQuery.getText().toString();
        myUpdateOperation();
    }

    public void subscriptions(View v){
        Intent intent = new Intent(this, SubscribedTopics.class);
        startActivity(intent);
    }

    public void myUpdateOperation() {
        try {
            topics = Converter.getWebLinks(client.discover(query));
        } catch (RuntimeException e) {

            Toast toast = Toast.makeText(this, "WRONG HOST , TIMEOUT", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
        final String[] stringTopics = new String[topics.size()];
        final String[] stringuri = new String[topics.size()];
        int i = 0;

        for (WebLink x : topics) {
            if (!x.toString().equals("</ps>\n\tct:\t[40]")) {
                stringTopics[i] = x.toString();
                stringuri[i] = x.getURI().substring(1) + "/";
                i++;
            }
        }

        if (stringTopics[0] != null) {

            // Capture the layout's listView and set the string array as its topics
            ArrayAdapter<String> displayTopics = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    stringTopics);
            listview.setAdapter(displayTopics);

            //make list clickable
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                    n.putExtra("topic-string", stringTopics[position]);
                    n.putExtra("topic-path", stringuri[position]);
                    n.putExtra("pubsub_client", client);
                    startActivity(n);
                }
            });

            mySwipeRefreshLayout.setRefreshing(false);
        }
    }


}
