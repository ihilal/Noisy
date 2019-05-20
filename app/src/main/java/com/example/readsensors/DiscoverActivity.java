package com.example.readsensors;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.californium.core.WebLink;

import java.util.ArrayList;
import java.util.Set;

public class DiscoverActivity extends AppCompatActivity {

    SwipeRefreshLayout mySwipeRefreshLayout;
    PubsubAndroid pubsub;

    ListView listview;
    Set<WebLink> topics;

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

        pubsub = new PubsubAndroid(intent.getStringExtra("address"));
        listview = findViewById(R.id.list);

        discover("");

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

    public void createMainTopic(View v) {
        Intent intent = new Intent(this, CreateTopicActivity.class);
        intent.putExtra("pubsub_client", pubsub);
        startActivity(intent);
        finish();
    }

    public void filter(View v) {
        EditText etQuery = findViewById(R.id.etQuery);
        discover(etQuery.getText().toString());
    }

    public void subscriptions(View v){
        Intent intent = new Intent(this, SubscribedTopics.class);
        startActivity(intent);
    }

    public void discover(String query) {
        try {
            topics = Converter.getAllWebLinks(pubsub.discover(query));

            final ArrayList<WebLink> topicList = new ArrayList<>(topics);

            // Capture the layout's listView and set the string array as its topics
            WebLinksAdapter webLinksAdapter = new WebLinksAdapter(this, topicList);
            listview.setAdapter(webLinksAdapter);

            //make list clickable
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    Intent n = new Intent(getApplicationContext(), TopicActivity.class);
                    WebLink webLink = topicList.get(position);
                    n.putExtra("topic-path", Converter.getUri(webLink));
                    n.putExtra("ct", Converter.getCT(webLink));
                    n.putExtra("pubsub_client", pubsub);
                    startActivity(n);
                }
            });

        } catch(RuntimeException e){
            Toast toast = Toast.makeText(this, "WRONG HOST , TIMEOUT", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            onBackPressed();
        }
    }

    public void myUpdateOperation() {
        discover("");
        mySwipeRefreshLayout.setRefreshing(false);
    }


}
