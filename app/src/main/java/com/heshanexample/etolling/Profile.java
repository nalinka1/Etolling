package com.heshanexample.etolling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    private TextView mTextMessage;
    ImageView userImage;
    Bitmap decodedBitmapOfImage;

    private String userFirstName;
    private String userLastName;
    private String userAddress;
    private String userIdNumber;
    private String userPhoneNumber;
    private String accountNumber;
    private String ownerName;
    private float balance;

    TextView profileDetails;

    private String userName;
    private int revisionNo;


    //textView variable
     TextView text1;
     TextView text2;
     TextView text3;
     TextView text4;
     TextView text5;
     TextView text6;
     TextView text7;

     TextView textshow1;
     TextView textshow2;
     TextView textshow3;
     TextView textshow4;
     TextView textshow5;
     TextView textshow6;
     TextView textshow7;

     private int vehicleCount;
     ArrayList allVehicles;
     private String contentAcc;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_info:
                    try {
                        userImage.setImageBitmap(decodedBitmapOfImage);
                        text1.setText("User Name");
                        text2.setText("First Name");
                        text3.setText("Last Name");
                        text4.setText("Phone Number");
                        text5.setText("Id Number");
                        text6.setText("Revision Number");
                        text7.setText("Address");

                        textshow1.setText(" : "+userName);
                        textshow2.setText(" : "+userFirstName);
                        textshow3.setText(" : "+userLastName);
                        textshow4.setText(" : "+userPhoneNumber);
                        textshow5.setText(" : "+userIdNumber);
                        textshow6.setText(" : "+Integer.toString(revisionNo));
                        textshow7.setText(" : "+userAddress);
                    }
                    catch (NullPointerException e){
                        text1.setText("User Name");
                        text2.setText("First Name");
                        text3.setText("Last Name");
                        text4.setText("Phone Number");
                        text5.setText("Id Number");
                        text6.setText("Revision Number");
                        text7.setText("Address");

                        textshow1.setText(" : "+userName);
                        textshow2.setText(" : "+userFirstName);
                        textshow3.setText(" : "+userLastName);
                        textshow4.setText(" : "+userPhoneNumber);
                        textshow5.setText(" : "+userIdNumber);
                        textshow6.setText(" : "+Integer.toString(revisionNo));
                        textshow7.setText(" : "+userAddress);
                    }
                    return true;
                case R.id.navigation_account:
                    try {
                        userImage.setImageBitmap(decodedBitmapOfImage);
                        text1.setText("Owner Name");
                        text2.setText("Account Number");
                        text3.setText("Balance");
                        text4.setText("");
                        text5.setText("");
                        text6.setText("");
                        text7.setText("");

                        textshow1.setText(" : "+ownerName);
                        textshow2.setText(" : "+accountNumber);
                        textshow3.setText(" : Rs. "+Double.toString(balance));
                        textshow4.setText("");
                        textshow5.setText("");
                        textshow6.setText("");
                        textshow7.setText("");
                    }
                    catch (NullPointerException e){
                        userImage.setImageBitmap(decodedBitmapOfImage);
                        text1.setText("Owner Name");
                        text2.setText("Account Number");
                        text3.setText("Balance");
                        text4.setText("");
                        text5.setText("");
                        text6.setText("");
                        text7.setText("");

                        textshow1.setText(" : "+ownerName);
                        textshow2.setText(" : "+accountNumber);
                        textshow3.setText(" : Rs. "+Double.toString(balance));
                        textshow4.setText("");
                        textshow5.setText("");
                        textshow6.setText("");
                        textshow7.setText("");
                    }
                    return true;
                case R.id.navigation_notifications:
                    text1.setText("");
                    text2.setText("");
                    text3.setText("");
                    text4.setText("");
                    text5.setText("");
                    text6.setText("");
                    text7.setText("");

                    textshow1.setText("");
                    textshow2.setText("");
                    textshow3.setText("");
                    textshow4.setText("");
                    textshow5.setText("");
                    textshow6.setText("");
                    textshow7.setText("");
                    try {
                        userImage.setImageResource(0);
                        if(vehicleCount>0){
                            HashMap vehicle1 = (HashMap)allVehicles.get(0);
                            text1.setText(vehicle1.get("className").toString());
                            textshow1.setText(":   "+vehicle1.get("vehicleNo"));
                            if(vehicleCount>1){
                                HashMap vehicle2 = (HashMap)allVehicles.get(1);
                                text2.setText(vehicle2.get("className").toString());
                                textshow2.setText(":   "+vehicle2.get("vehicleNo"));
                                if(vehicleCount>2){
                                    HashMap vehicle3 = (HashMap)allVehicles.get(2);
                                    text3.setText(vehicle3.get("className").toString());
                                    textshow3.setText(":   "+vehicle3.get("vehicleNo"));
                                    if(vehicleCount>3){
                                        HashMap vehicle4 = (HashMap)allVehicles.get(3);
                                        text4.setText(vehicle4.get("className").toString());
                                        textshow4.setText(":   "+vehicle4.get("vehicleNo"));
                                        if(vehicleCount>4){
                                            HashMap vehicle5 = (HashMap)allVehicles.get(4);
                                            text5.setText(vehicle5.get("className").toString());
                                            textshow5.setText(":   "+vehicle5.get("vehicleNo"));
                                            if(vehicleCount>5){
                                                HashMap vehicle6 = (HashMap)allVehicles.get(5);
                                                text6.setText(vehicle6.get("className").toString());
                                                textshow6.setText(":   "+vehicle6.get("vehicleNo"));
                                                if(vehicleCount>6){
                                                    text7.setText("More vehicle");
                                                    contentAcc="check Home selection list";
//                                                    for(int i =6; i<allVehicles.size();i++){
//                                                        HashMap a_vehicle_details = (HashMap) allVehicles.get(i);
//                                                        contentAcc+="\n"+a_vehicle_details.get("vehicleNo")+" : "+a_vehicle_details.get("className");
//                                                    }
                                                    textshow7.setText(contentAcc);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for(int i =0; i<allVehicles.size();i++){
                            HashMap a_vehicle_details = (HashMap) allVehicles.get(i);
                            //contentAcc+="\n"+a_vehicle_details.get("vehicleNo")+" : "+a_vehicle_details.get("className");
                        }

                    }
                    catch (NullPointerException e){

                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        // Update Button ...........

        Button updateButoon = (Button)findViewById(R.id.Update);
        updateButoon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Intent RegisterMe = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open-road-tolling.herokuapp.com/login"));
                startActivity(RegisterMe);
            }
        });



        SharedPreferences getDetails = getSharedPreferences("UserData",0);

        userFirstName = getDetails.getString("first_name",null);
        userLastName = getDetails.getString("last_name",null);
        userAddress = getDetails.getString("address",null);
        userIdNumber=getDetails.getString("id_number",null);
        userPhoneNumber= getDetails.getString("phone_number",null);
        accountNumber=getDetails.getString("account_number",null);
        ownerName= getDetails.getString("owner_name",null);
        balance=getDetails.getFloat("balance",00);
        userName= getDetails.getString("user_name",null);
        revisionNo=getDetails.getInt("revision_number",0);

        //get vehicle details to a list contains hashMap
        Gson gson = new Gson();
        String json = getDetails.getString("vehicle", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        allVehicles =(ArrayList) gson.fromJson(json, type);
        vehicleCount= allVehicles.size();

        text1 = findViewById(R.id.tx1);
        text2 = findViewById(R.id.tx2);
        text3 = findViewById(R.id.tx3);
        text4 = findViewById(R.id.tx4);
        text5 = findViewById(R.id.tx5);
        text6 = findViewById(R.id.tx6);
        text7 = findViewById(R.id.tx7);

        textshow1 = findViewById(R.id.txshow1);
        textshow2 = findViewById(R.id.txshow2);
        textshow3 = findViewById(R.id.txshow3);
        textshow4 = findViewById(R.id.txshow4);
        textshow5 = findViewById(R.id.txshow5);
        textshow6 = findViewById(R.id.txshow6);
        textshow7 = findViewById(R.id.txshow7);

        text1.setText("User Name");
        text2.setText("First Name");
        text3.setText("Last Name");
        text4.setText("Phone Number");
        text5.setText("Id Number");
        text6.setText("Revision Number");
        text7.setText("Address");

        textshow1.setText(" : "+userName);
        textshow2.setText(" : "+userFirstName);
        textshow3.setText(" : "+userLastName);
        textshow4.setText(" : "+userPhoneNumber);
        textshow5.setText(" : "+userIdNumber);
        textshow6.setText(" : "+Integer.toString(revisionNo));
        textshow7.setText(" : "+userAddress);




        String pureIm =getDetails.getString("encoded_image",null);
        if(pureIm.length()>20){
            final byte[] decodedBytes = Base64.decode(pureIm, Base64.DEFAULT);
            decodedBitmapOfImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


            userImage = (ImageView) findViewById(R.id.profileImage);
            userImage.setImageBitmap(decodedBitmapOfImage);
        }
    }

}
