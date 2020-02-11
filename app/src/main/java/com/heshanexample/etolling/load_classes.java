package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class load_classes extends AppCompatActivity {

    JsonSignInApi getclass;
    ArrayList got_classes;
    private String correct_address;
    private String correct_password;
    String MacListString;
    private int mode_id;
    private int Vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_classes);
        // get data from previous intent
        Bundle extras = getIntent().getExtras();
        MacListString = extras.getString("macAddressListB",null);
        mode_id= extras.getInt("mode",1);
        Vehicle=extras.getInt("vehicle",10);


        //get email and password
        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        correct_address = getDetails.getString("user_email",null);
        correct_password= getDetails.getString("user_password",null);

        /// get vehicle classes ......................

        Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create())
                .build();
        getclass = retro.create(JsonSignInApi.class);
        getVehicleClasses Classes = new getVehicleClasses(correct_address,correct_password);
        Call<getVehicleClasses> calls = getclass.getClasses(Classes);
        calls.enqueue(new Callback<getVehicleClasses>() {
            @Override
            public void onResponse(Call<getVehicleClasses> call, Response<getVehicleClasses> response) {
                if(!response.isSuccessful()){
                    return;
                }
                getVehicleClasses received_classes = response.body();
                got_classes= (ArrayList)received_classes.getVehicleClasses();
                Intent get_vehicle= new Intent(load_classes.this,addVehicle.class);
                get_vehicle.putExtra("mode",mode_id);
                get_vehicle.putExtra("macAddressListB",MacListString);
                get_vehicle.putExtra("vehicle",Vehicle);
                get_vehicle.putStringArrayListExtra("classes",got_classes);
                startActivity(get_vehicle);
                finish();

            }

            @Override
            public void onFailure(Call<getVehicleClasses> call, Throwable t) {
                Intent fail = new Intent(load_classes.this,InternetFailure.class);
                startActivity(fail);
                finish();
            }
        });
    }
}
