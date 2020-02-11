package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class history extends AppCompatActivity {
    private String correct_address;
    private String correct_password;

    JsonSignInApi requesthistory;
    String MacListString;
    ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        correct_address = getDetails.getString("user_email",null);
        correct_password= getDetails.getString("user_password",null);


        historyList =(ListView)findViewById(R.id.allHistory);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }


        Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create())
                .build();
        requesthistory= retro.create(JsonSignInApi.class);
        historyCall his = new historyCall(correct_address,correct_password);
        Call<List<historyCall>> call = requesthistory.history(his);
        call.enqueue(new Callback<List<historyCall>>() {
            @Override
            public void onResponse(Call<List<historyCall>> call, Response<List<historyCall>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<historyCall> receivedhistory=response.body();
                if(receivedhistory.size()==0){
                    List<String> noHis=new LinkedList<>();
                    noHis.add("No history");
                    ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,noHis);
                    historyList.setAdapter(adapter);
                }
                else{
                    List<String> myHis=new LinkedList<>();
                    for(historyCall myHistories: receivedhistory){
                        myHis.add(myHistories.toString());

                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,myHis);
                    historyList.setAdapter(adapter);
                }


            }

            @Override
            public void onFailure(Call<List<historyCall>> call, Throwable t) {

            }
        });





    }

}
