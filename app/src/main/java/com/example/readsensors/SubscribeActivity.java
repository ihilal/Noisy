package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class SubscribeActivity extends AppCompatActivity {

    ListView listview;
    SharedPreferences prefs;
    String address;
    ArrayList<String> dataArray= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);


        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        address = prefs.getString("address", "");

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String path = intent.getStringExtra("topic-path");

        listview = findViewById(R.id.listSubscribe);

        final SubscribeListener data = PubSub.subscribe(address, 5683, path);

        final ArrayAdapter<String> displayData = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataArray);
        listview.setAdapter(displayData);

        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");


        data.setListener(new SubscribeListener.ChangeListener() {
            @Override
            public void onChange() {
                String time = sdf.format(Calendar.getInstance().getTime());
                dataArray.add(time + ":   " + data.getData());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayData.notifyDataSetChanged();
                        listview.setSelection(displayData.getCount() - 1);
                    }
                });

            }
        });
    }
}
