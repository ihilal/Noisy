package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class CreateTopicActivity extends AppCompatActivity {

    String path = "ps/";

    PubsubAndroid client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic_activity);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        client = bundle.getParcelable("pubsub_client");


        if (intent.hasExtra("topic-path")) {
            path = intent.getStringExtra("topic-path");
        }

    }

    public void create(View v){
        EditText etName = (EditText) findViewById(R.id.etName);
        String name = etName.getText().toString();
        EditText etCT = (EditText) findViewById(R.id.etCT);
        int ct = Integer.parseInt(etCT.getText().toString());

        String res = null;
        try {
             res = client.create(name,ct, path).getResponseText();
        } catch (RuntimeException e) {
            Toast toast = Toast.makeText(this, "WRONG HOST , TIMEOUT", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


        Toast toast = Toast.makeText(CreateTopicActivity.this, res , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        startActivity(new Intent(CreateTopicActivity.this, DiscoverActivity.class));

        finish();
    }
}
