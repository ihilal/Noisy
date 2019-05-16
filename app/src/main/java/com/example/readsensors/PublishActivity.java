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


    String path = "";
    PubsubAndroid client;
    Topic topic;
    String stringTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        stringTopic = intent.getStringExtra("topic-string");
        path = intent.getStringExtra("topic-path");
        Bundle bundle = getIntent().getExtras();

        client = bundle.getParcelable("pubsub_client");
        topic = new Topic(new WebLink(path));
    }

    public void publish(View v) {
        EditText etContent = (EditText) findViewById(R.id.etContent);
        String content = etContent.getText().toString();

        String res = null;
        try {
            res = client.publish(path, content, topic.getCt());
        } catch (IOException e) {
            Toast toast = Toast.makeText(PublishActivity.this, res, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra("topic-path", path);
        intent.putExtra("pubsub_client",client);
        intent.putExtra("topic-string", stringTopic);
        startActivity(intent);

        finish();
    }
}
