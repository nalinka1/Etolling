package com.heshanexample.etolling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private int balance;

    TextView profileDetails;

    private String userName;
    private int revisionNo;

    String content;
    String contentAcc="";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_info:
                    try {
                        userImage.setImageBitmap(decodedBitmapOfImage);
                        profileDetails.setText(content);
                    }
                    catch (NullPointerException e){
                        profileDetails.setText(content);
                       // userImage.setVisibility(ImageView.VISIBLE);
                    }
                    return true;
                case R.id.navigation_account:
                    try {
                        userImage.setImageResource(0);
                        profileDetails.setText(contentAcc);
                    }
                    catch (NullPointerException e){
                        profileDetails.setText(contentAcc);
                        //userImage.setVisibility(ImageView.INVISIBLE);
                    }
                    return true;
                case R.id.navigation_notifications:
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
        balance=getDetails.getInt("balance",0);
        userName= getDetails.getString("user_name",null);
        revisionNo=getDetails.getInt("revision_number",0);

        profileDetails = (TextView) findViewById(R.id.userDetails);
        profileDetails.setPadding(2,2,2,2);
        content ="";
        content+= "User Name    : "+userName+"\n";
        content+= "First Name   : "+userFirstName+"\n";
        content+= "Last Name    : "+userLastName+"\n";
        content+= "Address  : "+ userAddress+"\n";
        content+= "Phone Number : "+userPhoneNumber+"\n";
        content+= " Id number  : "+userIdNumber+"\n";
        content+= " revision NUmber : "+ revisionNo+"\n";

        contentAcc+="";
        contentAcc+="Account Number : "+accountNumber+"\n";
        contentAcc+="Owner Name : "+ownerName+"\n";
        contentAcc+="Balance :"+balance+"\n";

        profileDetails.setText(content);
        profileDetails.setGravity(Gravity.CENTER_HORIZONTAL);

        String pureIm =getDetails.getString("encoded_image",null);
        if(pureIm.length()>20){
            final byte[] decodedBytes = Base64.decode(pureIm, Base64.DEFAULT);
            decodedBitmapOfImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


            userImage = (ImageView) findViewById(R.id.profileImage);
            userImage.setImageBitmap(decodedBitmapOfImage);
        }
    }

}
