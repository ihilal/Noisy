package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }


    public void edit(View v){


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        EditText etport = (EditText) findViewById(R.id.port_num);
        int portNum = Integer.parseInt(etport.getText().toString());

        intent.putExtra("port-num", portNum);

        startActivity(intent);

    }

    public void info(View v){
        Toast toast = Toast.makeText(this, "The default port number for CoAP PUB/SUB using UDP is 5683", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

}
