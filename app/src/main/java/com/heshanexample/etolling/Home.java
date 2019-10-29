package com.heshanexample.etolling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView userNameShow,userEmailShow;
    Intent main_activity;
    String user_name,user_email,password;

    WifiManager wifiManager;

    //wifi data

    TextView textView3;

    TextView textView21;
    TextView textView22;
    TextView textView23;
    TextView textView24;
    TextView textView25;


    String[] macAddressList;
    //String[] macAddressList = {"50:04:b8:17:6d:85","50:04:b8:17:6d:09","82:ce:b9:92:fa:c3"};
    List<String> received_Ap = new ArrayList<String>();
    String prev_ap="";
    String ap_recieved_time;
    String ap;
    boolean new_ap;
    vehicleDetails vehicleDetails = new vehicleDetails();
    boolean has_pre_app = false;
    int entrance_timeout = 60000;
    int exit_timeout = 60000;
    String last_trip_ap = "";
    String last_trip_ap_time = "";
    String TGAP_MacAddress;

    private String HIGHWAYSTATUS="";
    private String highwayStatus = "Away";
    private String entranceGate ="--";
    private String entranceTime ="--";
    private String exitGate ="--";
    private String exitTime="--";
    private float toll_fee = 0;
    String MacListString;


    SharedPreferences sharedPreferences2;
    MyBroadcasrReceiver myBroadcasrReceiver;

    boolean checkEnter=true;
    boolean checkExit =false;
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
    private int mode_id=1;

    private int revisionNumber;
    int[] a;

    JsonSignInApi jsUpdate ;

    private boolean checkLoc=false;
    //TextView myDistance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // set event in snacBar
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showCurrent = new Intent(Home.this,showCurrentData.class);
                showCurrent.putExtra("HIGHWAY_STATUS",HIGHWAYSTATUS);
                showCurrent.putExtra("highway_status",highwayStatus);
                showCurrent.putExtra("entrance_gate",entranceGate);
                showCurrent.putExtra("entrance_time",entranceTime);
                showCurrent.putExtra("exit_gate",exitGate);
                showCurrent.putExtra("exit_time",exitTime);
                showCurrent.putExtra("toll_fee",toll_fee);
                startActivity(showCurrent);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //setting navigation menu item icons
        Menu menu = navigationView.getMenu();
        MenuItem navVehicle = menu.findItem(R.id.nav_vehicle);
        navVehicle.setIcon(R.drawable.vehicle);
        MenuItem navHistory = menu.findItem(R.id.nav_History);
        navHistory.setIcon(R.drawable.history);
        MenuItem navRefresh = menu.findItem(R.id.nav_refresh);
        navRefresh.setIcon(R.drawable.refresh);
        MenuItem navProfile = menu.findItem(R.id.nav_Profile);
        navProfile.setIcon(R.drawable.user2);
        MenuItem navRecharge = menu.findItem(R.id.nav_recharge);
        navRecharge.setIcon(R.drawable.recharge);

        // wifi ..............ishan..
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }
        macAddressList = MacListString.split("\n");

        sharedPreferences2 = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
        prev_ap = sharedPreferences2.getString("prev_ap","");
        ap_recieved_time = sharedPreferences2.getString("ap_received_time",null);
        ap = sharedPreferences2.getString("ap",null);
        new_ap = sharedPreferences2.getBoolean("new_ap",false);
        has_pre_app = sharedPreferences2.getBoolean("has_pre_app",false);
        TGAP_MacAddress = sharedPreferences2.getString("TGAP_macAddress",null);
        vehicleDetails.setExit_time(sharedPreferences2.getString("Exit_time",null));
        vehicleDetails.setExit_gate(sharedPreferences2.getString("Exit_gate","---"));
        vehicleDetails.setTimeout(sharedPreferences2.getBoolean("Timeout",false));
        vehicleDetails.setStatus(sharedPreferences2.getBoolean("status",false));
        vehicleDetails.setDirection(sharedPreferences2.getString("Direction",null));
        vehicleDetails.setEntrance_time(sharedPreferences2.getString("entrance_time",null));
        vehicleDetails.setEntrance_gate(sharedPreferences2.getString("entrance_gate","---"));

        //String sfr = vehicleDetails.getStatus().toString();
        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        Gson gson9 = new Gson();
        String json9 = sharedPreferences2.getString("received_ap", null);
        Type type9 = new TypeToken<ArrayList<String>>() {}.getType();
        received_Ap =(ArrayList) gson9.fromJson(json9, type9);

        textView3 = (TextView)findViewById(R.id.textView3);
        //textView3.setText(MacListString);
        textView21 = (TextView)findViewById(R.id.highwayStatus);
        textView22 = (TextView)findViewById(R.id.entranceGate);
        textView23 = (TextView)findViewById(R.id.entranceTime);
        textView24 = (TextView)findViewById(R.id.exitGate);
        textView25 = (TextView)findViewById(R.id.exitTime);

        ///////////////////////////

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
        vehicle_drop.setSelection(0, true);
        View v = vehicle_drop.getSelectedView();
        ((TextView)v).setTextColor(Color.YELLOW);
        ((TextView)v).setTextSize(20);

        // setting onselectItem listner on the vehicle adddrp menu
        vehicle_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int vehicleIndex = parent.getSelectedItemPosition();
                SharedPreferences storeVehiclePosition = getApplicationContext().getSharedPreferences("UserData",0);
                SharedPreferences.Editor editPosition = storeVehiclePosition.edit();
                editPosition.putInt("vehicle_index",a[vehicleIndex]);
                ((TextView) view).setTextColor(Color.YELLOW);
                ((TextView) view).setTextSize(20);
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
    /*@Override
    protected void onStop() {
        unregisterReceiver(myBroadcasrReceiver);
        super.onStop();
    }*/


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
            Boolean setPrevAP = false;

            ////////////// to get mac address list /////////////////////////////
                /*for(ScanResult scanResult:list){
                    stringBuffer.append(scanResult.SSID+"\n"+scanResult.BSSID+"\n"+"\n");
                    stringBuffer1.append(scanResult.BSSID);
                    //textView1.setText(scanResult.BSSID);

                    if (scanResult.BSSID.contains("82:ce:b9:92:fa:c3")){
                        //textView1.setText("Scaned Ishan");
                        Toast.makeText(Home.this,"Scanned Ishan",Toast.LENGTH_LONG).show();
                    }
                    else {
                        //textView1.setText("scanning");
                    }
                }*/
            /////////////  to get highway status  //////////////////////////////
            for(ScanResult scanResult:list){
                stringBuffer.append(scanResult.SSID+"\n"+scanResult.BSSID+"\n"+"\n");

                for (String macID :macAddressList){

                    if (scanResult.BSSID.contains(macID/*macID.toString*/)){
                        Toast.makeText(Home.this,"AP Found",Toast.LENGTH_SHORT).show();
                        setPrevAP=true;
                        /////////////////adayage algorithm eka//////////////////
                        ap = scanResult.SSID;
                        if (!scanResult.BSSID.equals(prev_ap)){
                            prev_ap = scanResult.BSSID;
                            ap = scanResult.SSID;
                            new_ap =true;
                            Date currentTime = Calendar.getInstance().getTime();
                            ap_recieved_time = String.valueOf(currentTime.getTime());
                        }

                    }
                }
                if (new_ap){
                    if (vehicleDetails.getStatus().booleanValue()){
                        if (!received_Ap.get(received_Ap.size()-2).equals(ap)){
                            String[] temp1 = ap.split("_");
                            String[] temp2 = received_Ap.get(received_Ap.size()-2).split("_");
                            if (temp1[0].equals(temp2[0])){
                                if (temp1[1].equals("TGAP")){
                                    textView3.setText("Decision Pending");
                                    vehicleDetails.setTimeout(true);
                                    received_Ap.add(ap);
                                    received_Ap.add(ap_recieved_time);
                                    TGAP_MacAddress=scanResult.BSSID;
                                }
                                else {
                                    String direct = "";
                                    vehicleDetails.setTimeout(false);
                                    textView3.setText("Continue on the highway");
                                    TGAP_MacAddress="";
                                    if (temp1[1].equals("SAP1") && temp2[1].equals("SAP2")){
                                        direct = "UP";
                                    }
                                    else{
                                        direct = "DOWN";
                                    }
                                }
                            }
                            else if(ap.contains("SAP")){
                                received_Ap.add(ap);
                                received_Ap.add(ap_recieved_time);
                            }

                        }
                    }
                    else {
                        if (has_pre_app){
                            String test1 = received_Ap.get(received_Ap.size()-2);
                            if (!received_Ap.get(received_Ap.size()-2).equals(ap)){
                                String[] temp1 = ap.split("_");
                                String[] temp2 = received_Ap.get(received_Ap.size()-2).split("_");
                                textView3.setText("Welcome to the Highway");
                                vehicleDetails.setStatus(true);
                                vehicleDetails.setEntrance_gate(temp2[0]);
                                vehicleDetails.setEntrance_time(received_Ap.get(received_Ap.size()-1));
                                TGAP_MacAddress="";
                                Intent music = new Intent(Home.this,welcome.class);
                                music.putExtra("macAddressListB",MacListString);
                                startActivity(music);

                                if (temp1[1].equals("SAP1")){
                                    vehicleDetails.setDirection("UP");
                                }
                                else if(temp1[1].equals("SAP2")){
                                    vehicleDetails.setDirection("DOWN");
                                }
                                new_ap = false;
                                vehicleDetails.setTimeout(false);
                            }
                        }
                        else if(ap.contains("TGAP")){
                            if (received_Ap==null){
                                received_Ap = new ArrayList<String>();
                            }
                            received_Ap.add(ap);
                            received_Ap.add(ap_recieved_time);
                            has_pre_app=true;
                            vehicleDetails.setTimeout(true);
                            TGAP_MacAddress=scanResult.BSSID;

                        }
                        else{
                            new_ap =false;
                        }
                    }
                    new_ap =false;
                }
                else{
                    if(vehicleDetails.getStatus().booleanValue()){
                        if (vehicleDetails.getTimeout().booleanValue()){
                            String[] temp1 = ap.split("_");
                            String[] temp2 = received_Ap.get(received_Ap.size()-2).split("_");
                            Date currentTime = Calendar.getInstance().getTime();
                            if ((currentTime.getTime()-Long.parseLong(received_Ap.get(received_Ap.size()-1)))>exit_timeout){
                                textView3.setText("Thank You. Come Again.");
                                highwayStatus= "out side the high way ";
                                prev_ap="";
                                vehicleDetails.setStatus(false);
                                vehicleDetails.setExit_gate(temp1[0]);
                                vehicleDetails.setExit_time(received_Ap.get(received_Ap.size()-1));
                                last_trip_ap = ap;
                                last_trip_ap_time = ap_recieved_time;
                                ap="";
                                received_Ap.clear();
                                ap_recieved_time = "";
                                has_pre_app = false;
                                new_ap = false;
                                vehicleDetails.setTimeout(false);
                                TGAP_MacAddress = "";
                                Intent music = new Intent(Home.this,thank.class);
                                music.putExtra("macAddressListB",MacListString);
                                startActivity(music);
                            }
                        }
                    }
                    else{
                        if (vehicleDetails.getTimeout().booleanValue()){
                            //textView3.setText("Hee");
                            Date currentTime = Calendar.getInstance().getTime();
                            if ((currentTime.getTime()-Long.parseLong(received_Ap.get(received_Ap.size()-1)))>entrance_timeout){
                                textView3.setText("Just passing");
                                has_pre_app = false;
                                received_Ap.clear();
                                ap_recieved_time = "";
                                new_ap = false;
                                vehicleDetails.setTimeout(false);
                            }
                        }
                    }
                }


                //stringBuffer1.append(scanResult.BSSID);
                //textView1.setText(scanResult.BSSID);

                    /*if (scanResult.BSSID.contains("82:ce:b9:92:fa:c3")){
                        //textView1.setText("Scaned Ishan");
                        Toast.makeText(Home.this,"Scanned Ishan",Toast.LENGTH_LONG).show();
                    }
                    else {
                        //textView1.setText("scanning");
                    }*/
            }

                /*if (!setPrevAP){
                    prev_ap="";
                }*/

            if(vehicleDetails.getStatus().booleanValue()){
                highwayStatus="Entered";
            }
            //delete this later
            /*if(checkEnter&&vehicleDetails.getStatus().booleanValue()){
                checkEnter=false;
                Intent music = new Intent(Home.this,welcome.class);
                music.putExtra("macAddressListB",MacListString);
                startActivity(music);
            }*/
            HIGHWAYSTATUS= textView3.getText().toString();
            entranceGate = vehicleDetails.getEntrance_gate();
            entranceTime = convMillToDate(vehicleDetails.getEntrance_time());
            exitGate = vehicleDetails.getExit_gate();
            exitTime = convMillToDate(vehicleDetails.getExit_time());

            textView21.setText(highwayStatus);
            textView22.setText(entranceGate);
            textView23.setText(entranceTime);
            textView24.setText(exitGate);
            textView25.setText(exitTime);

            sharedPreferences2 = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences2.edit();

            edit.putString("prev_ap",prev_ap);
            edit.putString("ap_received_time",ap_recieved_time);
            edit.putString("ap",ap);
            edit.putBoolean("new_ap",new_ap);
            edit.putBoolean("has_pre_app",has_pre_app);
            edit.putString("TGAP_macAddress",TGAP_MacAddress);
            edit.putString("Exit_time",vehicleDetails.getExit_time());
            edit.putString("Exit_gate",vehicleDetails.getExit_gate());
            edit.putBoolean("Timeout",vehicleDetails.getTimeout());
            edit.putBoolean("status",vehicleDetails.getStatus());
            edit.putString("Direction",vehicleDetails.getDirection());
            edit.putString("entrance_time",vehicleDetails.getEntrance_time());
            edit.putString("entrance_gate",vehicleDetails.getEntrance_gate());

            edit.commit();

            //SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor edits = sharedPreferences2.edit();
            Gson gson1 = new Gson();
            String json1 = gson1.toJson(received_Ap);
            edits.putString("received_ap", json1);
            edits.apply();




                /*textView2.setText("Highway status :"+"\t"+HWS+"\n"+"Entrance gate :"+"\t"+vehicleDetails.getEntrance_gate()+"\n"+"Entrance time :"+"\t"+convMillToDate(vehicleDetails.getEntrance_time())
                        +"\n"+"Exit gate :"+"\t"+vehicleDetails.getExit_gate()+"\n"+"Exit time :"+"\t"+convMillToDate(vehicleDetails.getExit_time()));*/

            //textView2.setText(stringBuffer);
            //PostWiFiMethod("asdf","qwer.com",stringBuffer);

        }
    }

    public class vehicleDetails{
        Boolean status = false;
        String direction = "none";
        String entrance_gate = "---";
        String entrance_time ;
        String exit_gate = "---";
        String exit_time ;
        Boolean timeout = false;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getEntrance_gate() {
            return entrance_gate;
        }

        public void setEntrance_gate(String entrance_gate) {
            this.entrance_gate = entrance_gate;
        }

        public String getEntrance_time() {
            return entrance_time;
        }

        public void setEntrance_time(String entrance_time) {
            this.entrance_time = entrance_time;
        }

        public String getExit_gate() {
            return exit_gate;
        }

        public void setExit_gate(String exit_gate) {
            this.exit_gate = exit_gate;
        }

        public String getExit_time() {
            return exit_time;
        }

        public void setExit_time(String exit_time) {
            this.exit_time = exit_time;
        }

        public Boolean getTimeout() {
            return timeout;
        }

        public void setTimeout(Boolean timeout) {
            this.timeout = timeout;
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
    public String convMillToDate(String millTime){
        if(millTime == null){
            return "---";
        }
        else{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String dateString = formatter.format(new Date(Long.parseLong(millTime)));
            return dateString;
        }

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
                myProfile.putExtra("mode",mode_id);
                startActivity(myProfile);
                return true;
            case R.id.action_vehicle:
                return true;
            case R.id.action_report:
                return true;
            case  R.id.action_Refresh:
                Intent refresh = new Intent(Home.this,updateData.class);
                refresh.putExtra("mode",mode_id);
                startActivity(refresh);
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
            myProfile.putExtra("mode",mode_id);
            startActivity(myProfile);

        } else if (id == R.id.nav_History) {
            Intent music = new Intent(Home.this,welcome.class);
            music.putExtra("macAddressListB",MacListString);
            music.putExtra("mode",mode_id);
            startActivity(music);

        }else if(id==R.id.nav_vehicle){
            Intent addVehicle = new Intent(Home.this,addVehicle.class);
            addVehicle.putExtra("mode",mode_id);
            startActivity(addVehicle);

        } else if (id == R.id.nav_refresh) {
            Intent refresh = new Intent(Home.this,updateData.class);
            refresh.putExtra("check_connection",0);
            refresh.putExtra("macAddressListB",MacListString);
            refresh.putExtra("mode",mode_id);
            startActivity(refresh);
            finish();

        } else if (id == R.id.nav_mode) {
            Intent change = new Intent(Home.this,Mode.class);
            startActivity(change);
            finish();

        }else if (id == R.id.nav_recharge) {
            Intent goRecharge = new Intent(Home.this,recharge.class);
            goRecharge.putExtra("mode",mode_id);
            startActivity(goRecharge);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contactUs) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
