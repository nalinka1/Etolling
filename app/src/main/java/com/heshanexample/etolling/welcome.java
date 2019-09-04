package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class welcome extends AppCompatActivity {
    private static int TIME_OUT=5000;
    private Handler mHandler = new Handler();
    private String MacListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // vibrating for 5 second
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(5000);
        }

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
