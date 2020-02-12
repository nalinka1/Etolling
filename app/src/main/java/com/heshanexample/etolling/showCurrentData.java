package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class showCurrentData extends AppCompatActivity {

    // data get from home activity
    private String HIGHWAYSTATUS="";
    private String highwayStatus = "Away";
    private String entranceGate ="--";
    private String entranceTime ="--";
    private String exitGate ="--";
    private String exitTime="--";
    private String toll_fee ;
    private String VehicleNumber;
    private String VehicleCategory;

    TextView HIGHWAY_STATUS;
    TextView entrance_gate;
    TextView entrance_time;
    TextView exit_gate;
    TextView exit_time;
    TextView toll_fee1;
    TextView VehicleNo;
    TextView VehicleCat;

    SharedPreferences sharedPreferences;

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
        toll_fee= extras.getString("toll_fee","--");

        sharedPreferences = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
        VehicleNumber = sharedPreferences.getString("vehicleNo","");
        VehicleCategory = sharedPreferences.getString("className","");

        HIGHWAY_STATUS = (TextView)findViewById(R.id.highway_status_current);
        entrance_gate = (TextView)findViewById(R.id.entrance_gate_current);
        entrance_time = (TextView) findViewById(R.id.enrance_time_current);
        exit_gate = (TextView)findViewById(R.id.exit_gate_current);
        exit_time = (TextView)findViewById(R.id.current_exit_time);
        toll_fee1 = (TextView)findViewById(R.id.toll_fee);
        VehicleNo = (TextView)findViewById(R.id.Vehicle_number);
        VehicleCat = (TextView)findViewById(R.id.Vehicle_category);

        HIGHWAY_STATUS.setText(highwayStatus);
        entrance_gate.setText(entranceGate);
        entrance_time.setText(entranceTime);
        exit_gate.setText(exitGate);
        exit_time.setText(exitTime);
        toll_fee1.setText("Rs. "+toll_fee);
        VehicleNo.setText(VehicleNumber);
        VehicleCat.setText(VehicleCategory);
    }
}
