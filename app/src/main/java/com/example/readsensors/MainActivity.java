package com.example.readsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText ipAddress = (EditText) findViewById(R.id.etAddress);
        ipAddress.setText("130.229.178.80");
    }

    public void discover(View v){
        Intent intent = new Intent(this, DiscoverActivity.class);
        EditText ipAddress = (EditText) findViewById(R.id.etAddress);
        String address = ipAddress.getText().toString();

        //save data
        SharedPreferences prefs = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("address", address);
        editor.commit();

        startActivity(intent);
    }

}
