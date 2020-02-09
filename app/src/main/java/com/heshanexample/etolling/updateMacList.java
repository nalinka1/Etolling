package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class updateMacList extends AppCompatActivity {

    private String user_email,password;
    private ArrayList macAddressListA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mac_list);

        SharedPreferences getup = getSharedPreferences("UserData",0);
        password= getup.getString("user_password",null);
        user_email = getup.getString("user_email",null);



        //String user_eml = "textishara@gmail.com";
        //String user_pw = "0000";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open-road-tolling.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonMacAPI jsonPlaceHolderApi = retrofit.create(JsonMacAPI.class);

        PostMacAddress post = new PostMacAddress(user_email,password);
        Call<PostMacAddress> call1 = jsonPlaceHolderApi.createPost(post);

        call1.enqueue(new Callback<PostMacAddress>() {
            @Override
            public void onResponse(Call<PostMacAddress> call, Response<PostMacAddress> response) {
                if (!response.isSuccessful()){
                    //textView3.setText("Code: "+response.code());
                }
                int macResCode = response.code();

                if (macResCode == 200){
                    PostMacAddress postResponse = response.body();
                    macAddressListA=(ArrayList) postResponse.getMacAddresses();
                    StringBuffer asd = new StringBuffer();
                    for (Object a:macAddressListA){
                        asd.append(a.toString()+"\n");
                    }
                    //gotMacAddresses=true;
                    String gotohomeMAC = asd.toString();

                    Intent goTPaydue = new Intent(updateMacList.this,checkDue.class);
                    goTPaydue.putExtra("macAddressListB",gotohomeMAC);
                    startActivity(goTPaydue);

                    //textView3.append(asd);
                }




            }

            @Override
            public void onFailure(Call<PostMacAddress> call, Throwable t) {
                Intent noInternet = new Intent(updateMacList.this,InternetFailure.class);
                startActivity(noInternet);
                finish();
                //textView3.setText(t.getMessage());
            }
        });

    }
}
