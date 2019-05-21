package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.californium.core.WebLink;

import java.io.IOException;

public class PublishActivity extends AppCompatActivity {


    PubsubAndroid client;
    String topicPath;
    String topicName;
    int topicCt;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra("topic-path", topicPath);
        intent.putExtra("pubsub_client",client);
        intent.putExtra("topic-string", topicName);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        // Get the Intent that started this activity
        Intent intent = getIntent();
       topicName = intent.getStringExtra("topic-string");
       topicPath = intent.getStringExtra("topic-path");
       topicCt = intent.getIntExtra("topic-ct",0);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        client = bundle.getParcelable("pubsub_client");

    }

    public void publish(View v) {
        EditText etContent = (EditText) findViewById(R.id.etContent);
        String content = etContent.getText().toString();

        String res = null;
        res = client.publish( content, topicCt,topicPath).getResponseText();

        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra("topic-path", topicPath);
        intent.putExtra("pubsub_client",client);
        intent.putExtra("topic-string", topicName);
        intent.putExtra("topic-ct", topicCt);
        startActivity(intent);

        finish();
    }
}
