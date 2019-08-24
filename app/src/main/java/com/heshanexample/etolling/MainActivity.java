package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    boolean goHome = false;
    Intent Home,signUp;
    String Encript_email,Encript_password,password;
    public String email;

    // https response for sign In details variables




    // home page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        email = getDetails.getString("user_email",null);
        password= getDetails.getString("user_password",null);


        if(email==null){

            signUp = new Intent(MainActivity.this,Signup.class);
            startActivity(signUp);
            finish();

        }
        else{

            Home = new Intent(MainActivity.this,updateData.class);
            startActivity(Home);
            finish();
        }
    }
    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
