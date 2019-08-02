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
    public String User_Name = "Nalinka Heshan";
    // home page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        Encript_email = getDetails.getString("user_email",null);
        Encript_password= getDetails.getString("user_password",null);

        if(Encript_email==null){

            signUp = new Intent(MainActivity.this,Signup.class);
            startActivity(signUp);
            finish();

        }
        else{

            email = decrypt(Encript_email);
            password = decrypt(Encript_password);

            Home = new Intent(MainActivity.this,Home.class);
            Home.putExtra("Email",email);
            Home.putExtra("user",User_Name);
            startActivity(Home);
            finish();
        }
    }
    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
