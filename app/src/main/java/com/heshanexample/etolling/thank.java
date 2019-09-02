package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class thank extends AppCompatActivity {
    private static int TIME_OUT=5000;
    private Handler mHandler = new Handler();
    private String MacListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }
        final MediaPlayer thank= MediaPlayer.create(thank.this,R.raw.thank);
        thank.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goHome = new Intent(thank.this,Home.class);
                goHome.putExtra("macAddressListB",MacListString);
                startActivity(goHome);
                finish();
            }
        },TIME_OUT);

    }
}
