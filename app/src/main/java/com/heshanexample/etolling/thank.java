package com.heshanexample.etolling;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class thank extends AppCompatActivity {
    private static int TIME_OUT=10000;
    //private Handler mHandler = new Handler();
    private String MacListString;

    private String password;
    private String user_email;
    private String timestamp;
    private String realTime;
    private String macAddress;
    private String apName;
    private String vehicleNo;
    private String gps;

    private LocationManager locationManager;
    private LocationListener listener;
    private Looper looper;

    Boolean offlineMode;

    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);

        // vibrating for 5 second
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(2000);
        }

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }
        final MediaPlayer thank= MediaPlayer.create(thank.this,R.raw.thank);
        thank.start();

        ////////////// get user data ////////////////

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        password= getDetails.getString("user_password",null);
        user_email = getDetails.getString("user_email",null);

        SharedPreferences getDetails2 = getSharedPreferences("TRIPDATA",0);
        timestamp = getDetails2.getString("timeStamp",null);
        macAddress= getDetails2.getString("TGAP_macAddress",null);
        apName= getDetails2.getString("TGAP_name",null);
        vehicleNo = getDetails2.getString("vehicleNo","-");

        realTime = convMillToDate(timestamp);
        /////////// send data to the server ///////////////

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());

                gps = lat+","+lon;
                //textView.setText(gps);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://open-road-tolling.herokuapp.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonWiFiAPI jsonPlaceHolderApi = retrofit.create(JsonWiFiAPI.class);

                PostWiFi post = new PostWiFi(user_email,password,realTime,vehicleNo,macAddress,apName,gps);
                Call<PostWiFi> call1 = jsonPlaceHolderApi.createPost(post);

                call1.enqueue(new Callback<PostWiFi>() {
                    @Override
                    public void onResponse(Call<PostWiFi> call, Response<PostWiFi> response) {
                        int macResCode = response.code();
                        if (macResCode==200){

                            sharedPreferences2 = getApplicationContext().getSharedPreferences("TRIPDATA", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sharedPreferences2.edit();
                            edit.putString("Highway_Status","Away");
                            edit.putString("Exit_time",timestamp);
                            PostWiFi receivedExit=response.body();
                            String A = receivedExit.getExit();
                            String B = receivedExit.getToll();
                            edit.putString("Exit_gate",A);
                            edit.putString("toll_fee",B);
                            edit.commit();

                            Intent goHome = new Intent(thank.this,Home.class);
                            goHome.putExtra("macAddressListB",MacListString);
                            startActivity(goHome);
                            finish();


                        }
                        PostWiFi postResponse = response.body();


                    }

                    @Override
                    public void onFailure(Call<PostWiFi> call, Throwable t) {
                        offlineMode = true;
                        sharedPreferences3 = getApplicationContext().getSharedPreferences("OFFMODE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences3.edit();
                        edit.putBoolean("offlineMode",offlineMode);
                        edit.putString("offModeExitTimestamp",timestamp);
                        edit.putString("offModeExitMacAddress",macAddress);
                        edit.putString("offmodeExitApName",apName);
                        edit.putString("offModeVehicleNo",vehicleNo);
                        edit.commit();
                        Intent noInternet = new Intent(thank.this,InternetFailure.class);
                        noInternet.putExtra("macAddressListB",MacListString);
                        startActivity(noInternet);
                        finish();
                    }
                });

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        looper = null;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestSingleUpdate("gps", listener, looper);
        //configure_button();




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
}
