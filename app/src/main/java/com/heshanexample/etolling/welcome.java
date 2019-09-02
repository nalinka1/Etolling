package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class welcome extends AppCompatActivity {
    private static int TIME_OUT=5000;
    private Handler mHandler = new Handler();
    private String MacListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }
        final MediaPlayer welcome= MediaPlayer.create(com.heshanexample.etolling.welcome.this,R.raw.welcome);
        welcome.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goHome = new Intent(welcome.this,Home.class);
                goHome.putExtra("macAddressListB",MacListString);
                startActivity(goHome);
                finish();
            }
        },TIME_OUT);
    }
}
