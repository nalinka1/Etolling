package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean goHome = false;
    private Intent Home,signUp;
    private String Encript_email,Encript_password,password;
    private String email;
    private int mode_id;
    private int Vehicle;
    String MacListString=null;


    // home page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get saved data if those are available
        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        email = getDetails.getString("user_email",null);
        password= getDetails.getString("user_password",null);
        mode_id=getDetails.getInt("mode",1);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            Vehicle = extras.getInt("vehicle",10);
            if(Vehicle==0){
                Vehicle=1;
            }
        }



        if(email==null){

            signUp = new Intent(MainActivity.this,Signup.class);
            startActivity(signUp);
            finish();
        }
        else{

            // get vehicle list
            Gson gson = new Gson();
            String json = getDetails.getString("vehicle", null);
            Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
            ArrayList allVehicles =(ArrayList) gson.fromJson(json, type);
            if(Vehicle==1){
                Home = new Intent(MainActivity.this, updateData.class);
                //Home = new Intent(MainActivity.this,Home.class);
                Home.putExtra("check_connection", 1);
                Home.putExtra("mode", mode_id);
                startActivity(Home);
                finish();

            }
            else if(allVehicles.size()==0||allVehicles==null) {
                Intent addVehicle = new Intent(MainActivity.this, load_classes.class);
                addVehicle.putExtra("mode", mode_id);
                addVehicle.putExtra("macAddressListB", MacListString);
                addVehicle.putExtra("vehicle",0);
                startActivity(addVehicle);
            }
            else{
                    Home = new Intent(MainActivity.this, updateData.class);
                    //Home = new Intent(MainActivity.this,Home.class);
                    Home.putExtra("check_connection", 1);
                    Home.putExtra("mode", mode_id);
                    startActivity(Home);
                    finish();

            }



        }
    }
    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
