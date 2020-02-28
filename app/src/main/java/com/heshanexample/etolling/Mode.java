package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Mode extends AppCompatActivity {

    private int check;
    private int mode_id;
    private Button highway;
    private Button parking;

    private int Vehicle;
    private String MacListString=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);



        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            Vehicle = extras.getInt("vehicle",10);
        }
        highway=(Button)findViewById(R.id.highway_mode);

        highway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode_id=1;
                SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                SharedPreferences.Editor edit = storeInput.edit();
                edit.putInt("mode",mode_id);
                edit.commit();

                if(Vehicle==0){
                    Intent addVehicle = new Intent(Mode.this, load_classes.class);
                    addVehicle.putExtra("mode", mode_id);
                    addVehicle.putExtra("macAddressListB", MacListString);
                    addVehicle.putExtra("vehicle",Vehicle);
                    startActivity(addVehicle);
                }else {
                    Intent highway = new Intent(Mode.this, updateData.class);
                    highway.putExtra("check_connection", 1);
                    highway.putExtra("mode", mode_id);
                    startActivity(highway);
                    finish();
                }
            }
        });


    }

}
