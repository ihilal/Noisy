package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String pos = intent.getStringExtra("position");
        String id = intent.getStringExtra("id");

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        textView.setText(name);
    }

    public void publish(View v){
        Intent intent = new Intent(this, PublishActivity.class);
        Button publish = (Button) findViewById(R.id.button3);
        startActivity(intent);
    }

    public void subscribe(View v){
        Intent intent = new Intent(this, SubscribeActivity.class);
        Button subscribe = (Button) findViewById(R.id.button4);
        startActivity(intent);
    }

    public void createSubTopic(View v){
        Intent intent = new Intent(this, CreateSubTopicActivirty.class);
        Button create = (Button) findViewById(R.id.button7);
        startActivity(intent);
    }

}
