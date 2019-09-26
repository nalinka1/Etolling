package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class updateData extends AppCompatActivity {

    private String password;
    private String user_email;
    private int revisionNumber;

    private String userFirstName;
    private String userLastName;
    private String userAddress;
    private String userIdNumber;
    private String userPhoneNumber;
    private String accountNumber;
    private String ownerName;
    private float balance;
    private String encodedImage;
    private String user_name;



    JsonSignInApi jsUpdate ;
    private int check;
    private int mode_id;
    private String MacListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        // get data from previous intent
        Bundle extras = getIntent().getExtras();
        check = extras.getInt("check_connection",0);
        MacListString = extras.getString("macAddressListB",null);
        mode_id= extras.getInt("mode",1);

        //Get pre variable for update paramenters
        SharedPreferences getup = getSharedPreferences("UserData",0);
        revisionNumber=getup.getInt("revision_number",0);
        password= getup.getString("user_password",null);
        user_email = getup.getString("user_email",null);

        // update user data...................................................................................................

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                .build();

        jsUpdate = retro.create(JsonSignInApi.class);
        getUpdate get = new getUpdate(user_email,password,revisionNumber);
        Call<getUpdate> call = jsUpdate.getUpdates(get);
        call.enqueue(new Callback<getUpdate>() {
            @Override
            public void onResponse(Call<getUpdate> call, Response<getUpdate> response) {
                if(!response.isSuccessful()){
                    return;
                }
                int updateCode = response.code();
                getUpdate signIndetails = response.body();
                if(updateCode== 200){
                    ObjectMapper mapper = new ObjectMapper();
                    //Log.d("tag",signIndetails.toString());

                    userFirstName = signIndetails.getFirstName();
                    userLastName= signIndetails.getLastName();
                    userAddress= signIndetails.getAddress();
                    userIdNumber=signIndetails.getIdNumber();
                    userPhoneNumber=signIndetails.getPhoneNumber();
                    revisionNumber= signIndetails.getRevisionNo();

                    user_name = userFirstName+" "+userLastName;

                    try {
                        String accountInString = mapper.writeValueAsString(response.body().getAccount());
                        account userAccount = mapper.readValue(accountInString,account.class);
                        accountNumber = userAccount.getAccountNo();
                        ownerName = userAccount.getOwnerName();
                        balance = userAccount.getBalance();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    getVehicle[] userVehicles = new Gson().fromJson(signIndetails.getVehicle().toString(), getVehicle[].class);
                    ArrayList list = new ArrayList();
                    for(getVehicle vehi : userVehicles){
                        HashMap<String , Object> getList = new HashMap<>();
                        getList.put("vehicleNo",vehi.getVehicleNo());
                        getList.put("className",vehi.getClassName());
                        list.add(getList);
                    }
                    SharedPreferences storeVehicle = getApplicationContext().getSharedPreferences("UserData",0);
                    SharedPreferences.Editor edits = storeVehicle.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    edits.putString("vehicle", json);
                    edits.apply();

                    ///////////////////////////////
                    ////image//////
                    String encodedIm= signIndetails.getImage();
                    String pureIm =encodedIm.substring(encodedIm.indexOf(",")  + 1);


                    SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                    SharedPreferences.Editor edit = storeInput.edit();
                    edit.putString("user_password",password);
                    edit.putString("user_email",user_email);
                    edit.putString("first_name",userFirstName);
                    edit.putString("last_name",userLastName);
                    edit.putString("id_number",userIdNumber);
                    edit.putString("phone_number",userPhoneNumber);
                    edit.putString("address",userAddress);
                    edit.putString("account_number",accountNumber);
                    edit.putString("owner_name",ownerName);
                    edit.putFloat("balance",balance);
                    edit.putString("encoded_image",pureIm);
                    edit.putString("user_name",user_name);
                    edit.putInt("revision_number",revisionNumber);
                    edit.putString("lat",Double.toString(0.0));
                    edit.putString("lon",Double.toString(0.0));

                    edit.commit();

                }
                else{
                    balance = signIndetails.getBalance();
                    SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                    SharedPreferences.Editor edit = storeInput.edit();
                    edit.putFloat("balance",balance);
                    edit.commit();

                }
                // calling home page if all are ok
                //highway
                if(mode_id==1){
                    Intent home = new Intent(updateData.this,updateMacList.class);
                    startActivity(home);
                    finish();
                }
                // parking
                else if(mode_id==2){
                    Intent parking = new Intent(updateData.this, Parking.class);
                    startActivity(parking);
                    finish();
                }


            }

            @Override
            public void onFailure(Call<getUpdate> call, Throwable t) {

                Intent noInternet = new Intent(updateData.this,InternetFailure.class);
                noInternet.putExtra("check_connection",check);
                noInternet.putExtra("macAddressListB",MacListString);
                startActivity(noInternet);
                finish();


            }
        });
    }
}
