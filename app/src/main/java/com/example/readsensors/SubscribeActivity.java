package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class SubscribeActivity extends AppCompatActivity {

    ListView listview;
    ArrayList<String> dataArray= new ArrayList<String>();
    PubsubAndroid client;

    PubsubAndroid.Subscription new_sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String path = intent.getStringExtra("topic-path");
        Bundle bundle = intent.getExtras();
        client = bundle.getParcelable("pubsub_client");

        listview = findViewById(R.id.listSubscribe);


        final ArrayAdapter<String> displayData = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataArray);
        listview.setAdapter(displayData);

        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");


        SubscribeListener listen = new SubscribeListener() {
            @Override
            public void onResponse(String responseText) {
                String time = sdf.format(Calendar.getInstance().getTime());
                dataArray.add(time + ":   " +responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayData.notifyDataSetChanged();
                        listview.setSelection(displayData.getCount() - 1);
                    }
                });
            }

            @Override
            public void onError() {


                Toast toast = Toast.makeText(SubscribeActivity.this, "ERROR", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
        };

        new_sub = client.new Subscription(path,listen);
        new_sub.subscribe();

    }

    public void unsubscribe(View v){

            new_sub.unsubscribe();

            Toast toast = Toast.makeText(SubscribeActivity.this, "UNSUBSCRIBED SUCCESSFULLY", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            startActivity(new Intent(this, DiscoverActivity.class));

    }
    
}
