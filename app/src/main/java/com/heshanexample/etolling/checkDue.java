package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class checkDue extends AppCompatActivity {
    private String correct_address;
    private String correct_password;
    private boolean Pay;
    private int fromPay=1;

    JsonSignInApi requestPending;
    String MacListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_due);


        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        correct_address = getDetails.getString("user_email",null);
        correct_password= getDetails.getString("user_password",null);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }


        Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create())
                .build();
        requestPending = retro.create(JsonSignInApi.class);
        pendingTransaction newCheck = new pendingTransaction(correct_address,correct_password);
        Call<pendingTransaction> call = requestPending.check(newCheck);
        call.enqueue(new Callback<pendingTransaction>() {
            @Override
            public void onResponse(Call<pendingTransaction> call, Response<pendingTransaction> response) {
                if(!response.isSuccessful()){
                    return;
                }
                pendingTransaction pt = response.body();
                Pay = pt.isPay();
                if(Pay){

                    Intent goHome = new Intent(checkDue.this,Home.class);
                    goHome.putExtra("macAddressListB",MacListString);
                    startActivity(goHome);
                    //finish();
                }
                else{
                    fromPay=1;
                    Intent goRecharge = new Intent(checkDue.this,recharge.class);
                    goRecharge.putExtra("macAddressListB",MacListString);
                    goRecharge.putExtra("pay",fromPay);
                    startActivity(goRecharge);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<pendingTransaction> call, Throwable t) {

            }
        });
    }

}
