package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private boolean goHome = false;
    private Intent Home,signUp;
    private String Encript_email,Encript_password,password;
    private String email;
    private int mode_id;


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


        if(email==null){

            signUp = new Intent(MainActivity.this,Signup.class);
            startActivity(signUp);
            finish();
        }
        else{
            Home = new Intent(MainActivity.this,updateData.class);
            //Home = new Intent(MainActivity.this,Home.class);
            Home.putExtra("check_connection",1);
            Home.putExtra("mode",mode_id);
            startActivity(Home);
            finish();
        }
    }
    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
