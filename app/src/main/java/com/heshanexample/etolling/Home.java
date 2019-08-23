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

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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
    private int balance;
    private String encodedImage;

    private int revisionNumber;

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
        Call<getUpdate>call = jsUpdate.getUpdates(get);
        call.enqueue(new Callback<getUpdate>() {
            @Override
            public void onResponse(Call<getUpdate> call, Response<getUpdate> response) {
                if(!response.isSuccessful()){
                    textView1.setText(" not successful Code : "+response.code());
                    return;
                }
                int updateCode = response.code();
                if(updateCode== 200){
                    ObjectMapper mapper = new ObjectMapper();
                    getUpdate signIndetails = response.body();

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

                    ///////////////////////////// get vehicle

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
                    edit.putInt("balance",balance);
                    edit.putString("encoded_image",pureIm);
                    edit.putString("user_name",user_name);
                    edit.putInt("revision_number",revisionNumber);

                    edit.commit();
                    textView1.setText("successful Code : "+response.code());
                    textView1.append("\n updated..");

                }
                else{
                    textView1.setText("nothing changed");

                }

            }

            @Override
            public void onFailure(Call<getUpdate> call, Throwable t) {
                Intent noInternet = new Intent(Home.this,InternetFailure.class);
                startActivity(noInternet);
                finish();


            }
        });

        // get saved data in the app

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        userFirstName = getDetails.getString("first_name",null);
        userLastName = getDetails.getString("last_name",null);
        userAddress = getDetails.getString("address",null);
        userIdNumber=getDetails.getString("id_number",null);
        userPhoneNumber= getDetails.getString("phone_number",null);
        accountNumber=getDetails.getString("account_number",null);
        ownerName= getDetails.getString("owner_name",null);
        balance=getDetails.getInt("balance",0);
        revisionNumber=getDetails.getInt("revision_number",0);
        password= getDetails.getString("user_password",null);
        user_email = getDetails.getString("user_email",null);




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

                if (scanResult.BSSID.contains("82:ce:b9:92:fa:c3")){
                    //textView1.setText("Scaned Ishan");
                    Toast.makeText(Home.this,"Scanned Ishan",Toast.LENGTH_LONG).show();
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
