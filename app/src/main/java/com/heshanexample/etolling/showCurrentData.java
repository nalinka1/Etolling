package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class showCurrentData extends AppCompatActivity {

    // data get from home activity
    private String HIGHWAYSTATUS="";
    private String highwayStatus = "Away";
    private String entranceGate ="--";
    private String entranceTime ="--";
    private String exitGate ="--";
    private String exitTime="--";
    private float toll_fee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_current_data);

        Bundle extras = getIntent().getExtras();
        HIGHWAYSTATUS = extras.getString("HIGHWAY_STATUS","");
        highwayStatus = extras.getString("highway_status","away");
        entranceGate = extras.getString("entrance_gate","--");
        entranceTime = extras.getString("entrance_time","--");
        exitGate = extras.getString("exit_gate","--");
        exitTime = extras.getString("exit_time","--");
        toll_fee= extras.getFloat("toll_fee",0);

    }
}
