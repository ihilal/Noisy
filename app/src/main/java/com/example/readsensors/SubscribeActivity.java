package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
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
import java.util.Observable;
import java.util.SimpleTimeZone;

public class SubscribeActivity extends AppCompatActivity {

    ListView listview;
    String path;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SubscribedTopics.class));
    }

    PubsubAndroid.Subscription new_sub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        path = intent.getStringExtra("topic-path");


        listview = findViewById(R.id.listSubscribe);


        ObservableArrayList<String> dataArray = ((DataArraySub) SubscribeActivity.this.getApplication()).getData(path);

        final ArrayAdapter<String> displayData = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataArray);
        listview.setAdapter(displayData);


        SubscribeListener ls = new SubscribeListener() {
            @Override
            public void onResponse(String responseText) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayData.notifyDataSetChanged();
                        listview.setSelection(displayData.getCount()-1);
                    }
                });
            }

            @Override
            public void onError() {

            }
        };

        ((DataArraySub) SubscribeActivity.this.getApplication()).setListener(ls);

    }

    public void unsubscribe(View v){
            new_sub=((DataArraySub) SubscribeActivity.this.getApplication()).getPubSub(path);
            new_sub.unsubscribe();
            ((DataArraySub) SubscribeActivity.this.getApplication()).removeDataArray(path);

            Toast toast = Toast.makeText(SubscribeActivity.this, "UNSUBSCRIBED SUCCESSFULLY", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            startActivity(new Intent(this, SubscribedTopics.class));

            finish();
    }
    
}
