package com.heshanexample.etolling;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView userNameShow,userEmailShow;
    Intent main_activity;
    String user_name,user_email,password;

    WifiManager wifiManager;
    TextView textView1;
    TextView textView2;


    // user data

    private String userFirstName;
    private String userLastName;
    private String userAddress;
    private String userIdNumber;
    private String userPhoneNumber;
    private String accountNumber;
    private String ownerName;
    private float balance;
    private String encodedImage;

    private int revisionNumber;
    int[] a;

    JsonSignInApi jsUpdate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // delete later
        textView1 = (TextView)findViewById(R.id.serverResponseTextView);
        textView2 = (TextView)findViewById(R.id.wifiListTextView);


        Toast.makeText(this, " welcome to home Page", Toast.LENGTH_LONG).show();

        //get Navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        // get saved data in the app

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        userFirstName = getDetails.getString("first_name",null);
        userLastName = getDetails.getString("last_name",null);
        userAddress = getDetails.getString("address",null);
        userIdNumber=getDetails.getString("id_number",null);
        userPhoneNumber= getDetails.getString("phone_number",null);
        accountNumber=getDetails.getString("account_number",null);
        ownerName= getDetails.getString("owner_name",null);
        balance=getDetails.getFloat("balance",0);
        revisionNumber=getDetails.getInt("revision_number",0);
        password= getDetails.getString("user_password",null);
        user_email = getDetails.getString("user_email",null);


        // get vehicle list
        Gson gson = new Gson();
        String json = getDetails.getString("vehicle", null);
        Type type = new TypeToken<ArrayList<HashMap>>() {}.getType();
        ArrayList allVehicles =(ArrayList) gson.fromJson(json, type);
        ArrayList<String> vehicle_drop_down = new ArrayList<>();
        a= new int[allVehicles.size()];
        int alreadySelectedIndex= getDetails.getInt("vehicle_index",0);
        a[0]=alreadySelectedIndex;
        HashMap selectedVehicle = (HashMap)allVehicles.get(alreadySelectedIndex);
        vehicle_drop_down.add(selectedVehicle.get("vehicleNo")+" : "+selectedVehicle.get("className"));
        int k=1;
        for(int i =0; i<allVehicles.size();i++){
            if(i!=alreadySelectedIndex){
                HashMap a_vehicle_details = (HashMap) allVehicles.get(i);
                vehicle_drop_down.add(a_vehicle_details.get("vehicleNo")+" : "+a_vehicle_details.get("className"));
                a[k]=i;
                k++;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicle_drop_down);
        Spinner vehicle_drop = (Spinner)findViewById(R.id.selectVehicle);
        vehicle_drop.setAdapter(adapter);

        // setting onselectItem listner on the vehicle adddrp menu
        vehicle_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int vehicleIndex = parent.getSelectedItemPosition();
                SharedPreferences storeVehiclePosition = getApplicationContext().getSharedPreferences("UserData",0);
                SharedPreferences.Editor editPosition = storeVehiclePosition.edit();
                editPosition.putInt("vehicle_index",a[vehicleIndex]);
                editPosition.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

// get header
        View navHeader = navigationView.getHeaderView(0);


        // set image .....
        String pureIm =getDetails.getString("encoded_image",null);
        if(pureIm.length()>20){
            final byte[] decodedBytes = Base64.decode(pureIm, Base64.DEFAULT);
            Bitmap decodedBitmapOfImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


            ImageView userImage = (ImageView) navHeader.findViewById(R.id.userImageView);
            userImage.setImageBitmap(decodedBitmapOfImage);
        }

        // set user name and email
        userNameShow = (TextView) navHeader.findViewById(R.id.UserNameTextView);
        userEmailShow=(TextView)navHeader.findViewById(R.id.UserEmailTextView);

        user_name = userFirstName+" "+userLastName;

        userNameShow.setText(user_name);
        userEmailShow.setText(user_email);




////////////////////////// wifi scanner \\\\\\\\\\\\\\\\\\\\

        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);


        if (!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            //textView1.setText("WiFi is ON");
        }
        MyBroadcasrReceiver myBroadcasrReceiver = new MyBroadcasrReceiver();
        registerReceiver(myBroadcasrReceiver,new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        loop a = new loop();
        a.start();

    }



    class loop extends Thread{
        public void run(){
            while (true){
                wifiManager.startScan();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyBroadcasrReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer1 = new StringBuffer();
            List<ScanResult> list = wifiManager.getScanResults();
            for(ScanResult scanResult:list){
                stringBuffer.append(scanResult.SSID+"\n"+scanResult.BSSID+"\n"+"\n");
                stringBuffer1.append(scanResult.BSSID);
                //textView1.setText(scanResult.BSSID);

                if (scanResult.BSSID.contains("d8:b0:4c:c5:51:e4")){
                    //textView1.setText("Scaned Ishan");
                    Toast.makeText(Home.this,"Scanned AP",Toast.LENGTH_LONG).show();
                }
                else {
                    //textView1.setText("scanning");
                }
            }
            textView2.setText(stringBuffer);
            PostWiFiMethod("asdf","qwer.com",stringBuffer);

        }
    }

/////////////////////// end of wifi part \\\\\\\\\\\\\\\\\\\\\\\\\\


    ////////////// POST WIFI METHOD///////////////
    public void PostWiFiMethod(String user, String mail, StringBuffer mac){
        String fName = user;
        String sName = mail;
        StringBuffer ag = mac;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typecode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonWiFiAPI jsonPlaceHolderApi = retrofit.create(JsonWiFiAPI.class);

        final PostWiFi post = new PostWiFi(fName,ag,sName);
        Call<PostWiFi> call = jsonPlaceHolderApi.createPost(post);

        call.enqueue(new Callback<PostWiFi>() {
            @Override
            public void onResponse(Call<PostWiFi> call, Response<PostWiFi> response) {
                if (!response.isSuccessful()){
                    //textView1.setText("Code: "+response.code());
                }

                PostWiFi postResponse = response.body();

                String content = "\n";
                        /*content += postResponse.getFirst_name()+"\n";
                        content += postResponse.getSecond_name()+"\n";
                        content += postResponse.getAge()+"\n\n";*/
                content += postResponse.getId()+"\n";
                //textView1.append(content);
            }

            @Override
            public void onFailure(Call<PostWiFi> call, Throwable t) {
                //textView1.setText(t.getMessage());
            }
        });


    }



    //////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent myProfile = new Intent(Home.this,Profile.class);
                startActivity(myProfile);
                return true;
            case R.id.action_report:
                return true;
            case R.id.action_logout:

                AlertDialog.Builder LogoutCondition = new AlertDialog.Builder(this);
                LogoutCondition.setMessage("Do you want to logout? If you logged out, you have to sign in next time");
                LogoutCondition.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                        SharedPreferences.Editor clearUserData = storeInput.edit();
                        clearUserData.remove("user_password");
                        clearUserData.remove("user_email");
                        clearUserData.remove("encoded_image");
                        clearUserData.remove("user_name");
                        clearUserData.remove("first_name");
                        clearUserData.remove("last_name");
                        clearUserData.remove("address");
                        clearUserData.remove("id_name");
                        clearUserData.remove("phone_number");
                        clearUserData.remove("account_number");
                        clearUserData.remove("owner_name");
                        clearUserData.remove("balance");
                        clearUserData.remove("encoded_image");
                        clearUserData.commit();

                        Intent logout = new Intent(Home.this,MainActivity.class);
                        startActivity(logout);
                        finish();
                    }
                });

                LogoutCondition.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog AlertLogout = LogoutCondition.create();
                AlertLogout.setTitle("Logout !!!");
                AlertLogout.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Profile) {
            // Handle the camera action
            Intent myProfile = new Intent(Home.this,Profile.class);
            startActivity(myProfile);

        } else if (id == R.id.nav_History) {

        } else if (id == R.id.nav_refresh) {

        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contactUs) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
