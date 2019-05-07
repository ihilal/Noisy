package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateSubTopicActivity extends AppCompatActivity {

    String path1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sub_topic_activirty);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String path = intent.getStringExtra("name");
        path1 = path;

    }

    public void create(View v){
        EditText textview = (EditText) findViewById(R.id.editText3);
        String name = textview.getText().toString();
        EditText textview1 = (EditText) findViewById(R.id.editText4);
        int ct = Integer.parseInt(textview1.getText().toString());

        //load data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", "");
        String response = PubSub.create(ip,5683,5000,PubSub.get_path(path1),name,ct);

        Toast toast = Toast.makeText(CreateSubTopicActivity.this, "jhj" , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 500);
        toast.show();
    }
}
