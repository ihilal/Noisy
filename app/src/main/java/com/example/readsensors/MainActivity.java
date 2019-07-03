package com.example.readsensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    int port = 5683;
    Map<String,String> broker = new HashMap<>();
    String address= "";
    String brokerName;
    String brokerAdress;
   AutoCompleteTextView autoBroker;

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.action_info:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/team-multicast/home"));
                startActivity(browserIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("data", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        if(intent.hasExtra("port-num"))
            port = intent.getIntExtra("port-num", 5683);


        broker = loadMap();

       /* EditText ipAddress = (EditText) findViewById(R.id.etAddress);
        if (prefs.contains("address"))
            ipAddress.setText(prefs.getString("address", ""));*/

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final String[] keys = broker.keySet().toArray(new String[broker.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,keys);
        autoBroker = findViewById(R.id.auto_broker);
        autoBroker.setThreshold(0);//will start working from first character
        autoBroker.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        autoBroker.setCompletionHint("choose a broker");
        autoBroker.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                autoBroker.showDropDown();
                autoBroker.requestFocus();
                return false;
            }
        });

        autoBroker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                address = broker.get(keys[position]);

            }
        });


        FloatingActionButton button = findViewById(R.id.floatingActionButton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View prompt = inflater.inflate(R.layout.prompts,null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setView(prompt);

                final EditText userInput = (EditText) prompt.findViewById(R.id.editTextDialogUserInput);
                final EditText userInput2 = (EditText) prompt.findViewById(R.id.editTextDialogUserInput2);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        brokerAdress = userInput.getText().toString();
                                        brokerName = userInput2.getText().toString();
                                        broker.put(brokerName,brokerAdress);
                                        updateBrokerList();

                                    }
                                });

                alertDialogBuilder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();


                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }


        });

        //add_broker(broker);




    }

    @SuppressLint("ClickableViewAccessibility")
    private void updateBrokerList() {


        final String[] keys = broker.keySet().toArray(new String[broker.keySet().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,keys);
        final AutoCompleteTextView autoBroker = findViewById(R.id.auto_broker);
        autoBroker.setThreshold(0);//will start working from first character
        autoBroker.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        autoBroker.setCompletionHint("choose a broker");

        autoBroker.setOnTouchListener(new View.OnTouchListener() {


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                autoBroker.showDropDown();
                autoBroker.requestFocus();
                return false;
            }
        });
        autoBroker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                address = broker.get(keys[position]);

            }
        });



    }

    public void discover(View v){

        final AutoCompleteTextView autoBroker = findViewById(R.id.auto_broker);
        if(!autoBroker.getText().toString().isEmpty()) {


            Intent intent = new Intent(this, DiscoverActivity.class);
            intent.putExtra("port-num", port);


            saveMap(broker);

            if (address.equals(""))
                address = autoBroker.getText().toString();


            //intent.putExtra("address",address);
            SharedPreferences.Editor editor1 = prefs.edit();
            editor1.putString("address", address);
            editor1.commit();


            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(this, "Choose a broker", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }



    private void saveMap(Map<String,String> inputMap){
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
        }
    }

    private Map<String,String> loadMap(){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("MyVariables", Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }

}
