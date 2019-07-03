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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class DiscoverActivity extends AppCompatActivity {

    SwipeRefreshLayout mySwipeRefreshLayout;
    SharedPreferences prefs;
    String address;
    ListView listview;
    Set<WebLink> topics;
    PubsubAndroid client;
    String query = "";
    ArrayList<String> pubTopics ;
    ArrayList<String> puburi;
    ArrayList<Integer> pubct;
    ArrayList<String> crTopics ;
    ArrayList<String> cruri;
    ArrayList<Integer> crct;
    int port = 5683;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);


/*        //expert mode
        if(((DataArraySub) DiscoverActivity.this.getApplication()).getMode())
            findViewById(R.id.button).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.button).setVisibility(View.INVISIBLE);
*/


        //load data
        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");

        Intent intent = getIntent();
       /* if(intent.hasExtra("address")) {
            address = intent.getStringExtra("address");

        }*/

        client = new PubsubAndroid(address);


        if(intent.hasExtra("port-num")) {
            port = intent.getIntExtra("port-num", 5683);
            client.setPort(port);
        }


        try {
            pubTopics = new ArrayList<>();
            puburi = new ArrayList<>();
            pubct = new ArrayList<>();
            crTopics = new ArrayList<>();
            cruri = new ArrayList<>();
            crct = new ArrayList<>();

            String broker = client.discover("rt=ps.core").getResponseText();
            Toast toast = Toast.makeText(this, "BROKER IS RUNNING PS\n" + broker, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            topics = Converter.getAllWebLinks(client.discover(query));

            for (WebLink x : topics) {
                if(Converter.getCT(x) == 40){
                    crTopics.add(Converter.getName(x));
                    cruri.add(Converter.getUri(x));
                    crct.add(Converter.getCT(x));
                } else{
                    pubTopics.add(Converter.getName(x));
                    puburi.add(Converter.getUri(x));
                    pubct.add(Converter.getCT(x));
                }

            }

                    // Capture the layout's listView and set the string array as its topics
                    listview = (ListView) findViewById(R.id.list);
                    ArrayAdapter<String> displayTopics = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,puburi);
                    listview.setAdapter(displayTopics);

                    //make list clickable
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                            Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                            n.putExtra("topic-string", pubTopics.get(position));
                            n.putExtra("topic-path", puburi.get(position));
                            n.putExtra("topic-ct", pubct.get(position));
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
        } catch (NullPointerException e) {
            Toast toast = Toast.makeText(this, "WRONG HOST , TIMEOUT", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            onBackPressed();
        }

    }

    public void createMainTopic(View v) {
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra("topic-string", crTopics);
        intent.putExtra("topic-path", cruri);
        intent.putExtra("topic-ct", crct);
        intent.putExtra("pubsub_client", client);

        startActivity(intent);
        finish();
    }

    public void filter(View v) {
        EditText etQuery = (EditText) findViewById(R.id.etQuery);
        query = etQuery.getText().toString().toLowerCase();
        myUpdateOperation();
    }

    public void find(View v){
        final EditText etQuery = (EditText) findViewById(R.id.etQuery);
        String key = etQuery.getText().toString();
        final ArrayList<String> resultTopic = new ArrayList<>();
        final ArrayList<String> resultUri = new ArrayList<>();
        final ArrayList<Integer> resultct = new ArrayList<>();


        for(int i =0 ; i<pubTopics.size();i++){
          if(pubTopics.get(i).contains(key)){

              resultTopic.add(pubTopics.get(i));
              resultUri.add(puburi.get(i));
              resultct.add(pubct.get(i));
          }

      }

        if(resultTopic.isEmpty()){
            Toast toast = Toast.makeText(this, "No results", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        listview = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> displayTopics = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,resultUri);
        listview.setAdapter(displayTopics);

        //make list clickable
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                n.putExtra("topic-string", resultTopic.get(position));
                n.putExtra("topic-path", resultUri.get(position));
                n.putExtra("topic-ct", resultct.get(position));
                n.putExtra("pubsub_client", client);
                etQuery.setText("");
                startActivity(n);
            }
        });


    }

    public void subscriptions(View v) {
        Intent intent = new Intent(this, SubscribedTopics.class);
        startActivity(intent);
    }



    public void myUpdateOperation() {
        try {

            pubTopics = new ArrayList<>();
            puburi = new ArrayList<>();
            pubct = new ArrayList<>();
            crTopics = new ArrayList<>();
            cruri = new ArrayList<>();
            crct = new ArrayList<>();


            String broker = client.discover("rt=ps.core").getResponseText();
            Toast toast = Toast.makeText(this, "BROKER IS RUNNING PS\n" + broker, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            topics = Converter.getAllWebLinks(client.discover(query));

            for (WebLink x : topics) {
                if(Converter.getCT(x) == 40){
                    crTopics.add(Converter.getName(x));
                    cruri.add(Converter.getUri(x));
                    crct.add(Converter.getCT(x));
                } else{
                    pubTopics.add(Converter.getName(x));
                    puburi.add(Converter.getUri(x));
                    pubct.add(Converter.getCT(x));
                }

            }

            // Capture the layout's listView and set the string array as its topics
            listview = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> displayTopics = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,puburi);
            listview.setAdapter(displayTopics);

            //make list clickable
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                    n.putExtra("topic-string", pubTopics.get(position));
                    n.putExtra("topic-path", puburi.get(position));
                    n.putExtra("topic-ct", pubct.get(position));
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

        } catch (NullPointerException e) {
            Toast toast = Toast.makeText(this, "WRONG HOST , TIMEOUT", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            onBackPressed();
        }

        mySwipeRefreshLayout.setRefreshing(false);
    }
}

