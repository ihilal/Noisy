package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PublishActivity extends AppCompatActivity {


    String path1 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        String path = intent.getStringExtra("name");
        path1 = path;


    }

    public void publish(View v){
        EditText textview = (EditText) findViewById(R.id.editText2);
        String name = textview.getText().toString();
        String response = PubSub.publish("130.229.134.2",5683,PubSub.get_path(path1),name,0);

//        TextView textview2 = (TextView) findViewById(R.id.textView2);
//        TextView textview3 = (TextView) findViewById(R.id.textView3);
//        textview2.setText(path1);
//        textview3.setText(PubSub.get_path(path1));

        Toast toast = Toast.makeText(PublishActivity.this, response , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 350, 500);
        toast.show();
    }
}
