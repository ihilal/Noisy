package com.example.readsensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switchMode = findViewById(R.id.switch1);
        switchMode.setChecked(((DataArraySub) SettingsActivity.this.getApplication()).getMode());

    }


    public void edit(View v){
        Switch switchMode = findViewById(R.id.switch1);


        boolean mode = switchMode.isChecked();
        ((DataArraySub) SettingsActivity.this.getApplication()).setMode(mode);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        int portNum;
        EditText etport = (EditText) findViewById(R.id.port_num);

        if(!etport.getText().toString().isEmpty()){
            portNum = Integer.parseInt(etport.getText().toString());
            intent.putExtra("port-num", portNum);
        }

        startActivity(intent);

    }

    public void info(View v){
        Toast toast = Toast.makeText(this, "The default port number for CoAP PUB/SUB using UDP is 5683", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 500);
        toast.show();
    }

    public void info2(View v){
        Toast toast = Toast.makeText(this, "Expert mode allows to create and remove topics and to publish to topics", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 500);
        toast.show();
    }
}
