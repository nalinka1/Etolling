package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class welcome extends AppCompatActivity {
    private static int TIME_OUT=10000;
    //private Handler mHandler = new Handler();
    private String MacListString;

    private String password;
    private String user_email;
    private String timestamp;
    private String macAddress;
    private String vehicleNo;

    Boolean offlineMode;

    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // vibrating for 5 second
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(2000);
        }

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }
        final MediaPlayer welcome= MediaPlayer.create(com.heshanexample.etolling.welcome.this,R.raw.welcome);
        welcome.start();


        ////////////// get user data ////////////////

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        password= getDetails.getString("user_password",null);
        user_email = getDetails.getString("user_email",null);
        SharedPreferences getDetails2 = getSharedPreferences("TRIPDATA",0);
        timestamp = getDetails2.getString("timeStamp",null);
        macAddress= getDetails2.getString("TGAP_macAddress",null);
        vehicleNo = getDetails2.getString("vehicleNo","-");

        /////////// send data to the server ///////////////
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open-road-tolling.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonWiFiAPI jsonPlaceHolderApi = retrofit.create(JsonWiFiAPI.class);

        PostWiFi post = new PostWiFi(user_email,password,timestamp,vehicleNo,macAddress);
        Call<PostWiFi> call1 = jsonPlaceHolderApi.createPost(post);

        call1.enqueue(new Callback<PostWiFi>() {
            @Override
            public void onResponse(Call<PostWiFi> call, Response<PostWiFi> response) {
                int macResCode = response.code();
                if (macResCode==200){

                    sharedPreferences2 = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences2.edit();
                    edit.putString("entrance_time",timestamp);
                    String A = response.body().getEntrance().toString();
                    edit.putString("entrance_gate",A);
                    edit.commit();

                    Intent goHome = new Intent(welcome.this,Home.class);
                    goHome.putExtra("macAddressListB",MacListString);
                    startActivity(goHome);
                    finish();

                    /*
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent goHome = new Intent(welcome.this,Home.class);
                            goHome.putExtra("macAddressListB",MacListString);
                            startActivity(goHome);
                            finish();
                        }
                    },TIME_OUT);*/

                }
                //PostWiFi postResponse = response.body();


            }

            @Override
            public void onFailure(Call<PostWiFi> call, Throwable t) {
                offlineMode = true;
                sharedPreferences3 = getApplicationContext().getSharedPreferences("OFFMODE", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences3.edit();
                edit.putBoolean("offlineMode",offlineMode);
                edit.putString("offModeEntranceTimestamp",timestamp);
                edit.putString("offModeEntranceMacAddress",macAddress);
                edit.putString("offModeVehicleNo",vehicleNo);
                edit.commit();
                Intent noInternet = new Intent(welcome.this,InternetFailure.class);
                noInternet.putExtra("macAddressListB",MacListString);
                startActivity(noInternet);
                finish();
            }
        });



    }
}
