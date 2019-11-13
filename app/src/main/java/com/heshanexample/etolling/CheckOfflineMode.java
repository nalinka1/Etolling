package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class CheckOfflineMode extends AppCompatActivity {

    private int check;
    private String MacListString;
    private Handler mHandler = new Handler();
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences3;
    Boolean offMode;
    String offModeEntranceMacAddress;
    String offModeEntranceTimestamp;
    String offModeExitTimestamp;
    String offModeExitMacAddress;

    private String password;
    private String user_email;
    private String vehicleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_offline_mode);

        Bundle extras = getIntent().getExtras();
        check = extras.getInt("check_connection",0);
        MacListString = extras.getString("macAddressListB",null);


        sharedPreferences1 = getApplicationContext().getSharedPreferences("OFFMODE", Context.MODE_PRIVATE);
        offMode = sharedPreferences1.getBoolean("offlineMode",false);
        offModeEntranceMacAddress = sharedPreferences1.getString("offModeEntranceMacAddress",null);
        offModeEntranceTimestamp = sharedPreferences1.getString("offModeEntranceTimestamp",null);
        offModeExitTimestamp = sharedPreferences1.getString("offModeExitTimestamp",null);
        offModeExitMacAddress = sharedPreferences1.getString("offModeExitMacAddress",null);

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        password= getDetails.getString("user_password",null);
        user_email = getDetails.getString("user_email",null);
        SharedPreferences getVehicle = getSharedPreferences("TRIPDATA",0);
        vehicleNo = getDetails.getString("vehicleNo",null);

        if (offMode){

            /////////// take this to the inside /////////////
            sharedPreferences3 = getApplicationContext().getSharedPreferences("OFFMODE", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences3.edit();
            edit.putBoolean("offlineMode",false);
            /////////////////////////////////////////////////

            if (offModeEntranceMacAddress!=null){

                //////
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://open-road-tolling.herokuapp.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonWiFiAPI jsonPlaceHolderApi = retrofit.create(JsonWiFiAPI.class);

                PostWiFi post = new PostWiFi(user_email,password,offModeEntranceTimestamp,vehicleNo,offModeEntranceMacAddress);
                Call<PostWiFi> call1 = jsonPlaceHolderApi.createPost(post);

                call1.enqueue(new Callback<PostWiFi>() {
                    @Override
                    public void onResponse(Call<PostWiFi> call, Response<PostWiFi> response) {
                        int macResCode = response.code();
                        if (macResCode==200){

                            SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences2.edit();
                            edit.putString("entrance_time",offModeEntranceTimestamp);
                            edit.putString("entrance_gate",response.body().getEntrance());
                            edit.commit();

                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent goHome = new Intent(CheckOfflineMode.this,Home.class);
                                    goHome.putExtra("macAddressListB",MacListString);
                                    startActivity(goHome);
                                    finish();
                                }
                            },TIME_OUT);

                        }
                    }

                    @Override
                    public void onFailure(Call<PostWiFi> call, Throwable t) {
                        Intent noInternet = new Intent(CheckOfflineMode.this,InternetFailure.class);
                        noInternet.putExtra("macAddressListB",MacListString);
                        startActivity(noInternet);
                        finish();
                    }
                });
                //////

            }
            if (offModeExitMacAddress!=null){
                ////////
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://open-road-tolling.herokuapp.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonWiFiAPI jsonPlaceHolderApi = retrofit.create(JsonWiFiAPI.class);

                PostWiFi post = new PostWiFi(user_email,password,offModeExitTimestamp,vehicleNo,offModeExitMacAddress);
                Call<PostWiFi> call1 = jsonPlaceHolderApi.createPost(post);

                call1.enqueue(new Callback<PostWiFi>() {
                    @Override
                    public void onResponse(Call<PostWiFi> call, Response<PostWiFi> response) {
                        int macResCode = response.code();
                        if (macResCode==200){

                            SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences2.edit();
                            edit.putString("Exit_time",offModeExitTimestamp);
                            edit.putString("Exit_gate",response.body().getExit());
                            edit.putString("toll_fee",response.body().getToll());
                            edit.commit();

                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent goHome = new Intent(CheckOfflineMode.this,Home.class);
                                    goHome.putExtra("macAddressListB",MacListString);
                                    startActivity(goHome);
                                    finish();
                                }
                            },TIME_OUT);

                        }
                        PostWiFi postResponse = response.body();


                    }

                    @Override
                    public void onFailure(Call<PostWiFi> call, Throwable t) {
                        Intent noInternet = new Intent(CheckOfflineMode.this,InternetFailure.class);
                        noInternet.putExtra("macAddressListB",MacListString);
                        startActivity(noInternet);
                        finish();
                    }
                });
                ////////

            }

        }
        else{
            Intent home = new Intent(CheckOfflineMode.this,updateMacList.class);
            startActivity(home);
            finish();
        }
    }
}
