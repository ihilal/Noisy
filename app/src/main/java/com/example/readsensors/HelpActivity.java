package com.example.readsensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        TextView tv = findViewById(R.id.help);
        tv.setText("This app is for the Multicast team's project for the course II1305 at KTH.\n" +
                "\n" +
                "The goal of the project is to implement the IETS's RFC for CoAP publish-subscribe as an API/library for general use alongside Californium, and then build a front-end client Android app to show live data published from a network of sensors through a broker to subscribed clients using the aforementioned API. The project is commissioned by the KTH Network Systems Laboratory. You can read more about it here: https://www.kth.se/cos/research/nslab\n" +
                "\n" +
                "You can take a look at the IETS's RFC for CoAP publish-subscribe draft here: https://www.ietf.org/id/draft-ietf-core-coap-pubsub-08.txt\n" +
                "\n" +
                "You can follow along our Project progress here: https://sites.google.com/view/team-multicast/home\n" +
                "\n" +
                "You can see the repository for the Android app here: https://github.com/ihilal/Noisy\n" +
                "\n" +
                "This library is meant to be used alongside The Californium Library.");
    }
}
