package com.heshanexample.etolling;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
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

public class Parking extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // user data
    private int mode_id=2;
    String user_name,user_email,password;
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
    TextView userNameShow,userEmailShow;
    Intent main_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
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

        //get Navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get header
        View navHeader = navigationView.getHeaderView(0);

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
    }

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
        getMenuInflater().inflate(R.menu.parking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent myProfile = new Intent(Parking.this,Profile.class);
                myProfile.putExtra("mode",mode_id);
                startActivity(myProfile);
                return true;
            case R.id.action_vehicle:
                return true;
            case R.id.action_report:
                return true;
            case  R.id.action_Refresh:
                Intent refresh = new Intent(Parking.this,updateData.class);
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

                        Intent logout = new Intent(Parking.this,MainActivity.class);
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
            Intent myProfile = new Intent(Parking.this,Profile.class);
            myProfile.putExtra("mode",mode_id);
            startActivity(myProfile);

        } else if (id == R.id.nav_History) {
            Intent music = new Intent(Parking.this,welcome.class);
            music.putExtra("mode",mode_id);
            startActivity(music);

        }else if(id==R.id.nav_vehicle){
            Intent addVehicle = new Intent(Parking.this,addVehicle.class);
            addVehicle.putExtra("mode",mode_id);
            startActivity(addVehicle);

        } else if (id == R.id.nav_refresh) {
            Intent refresh = new Intent(Parking.this,updateData.class);
            refresh.putExtra("check_connection",0);
            refresh.putExtra("mode",mode_id);
            startActivity(refresh);
            finish();

        } else if (id == R.id.nav_mode) {
            Intent change = new Intent(Parking.this,Mode.class);
            startActivity(change);
            finish();

        }else if (id == R.id.nav_recharge) {
            Intent goRecharge = new Intent(Parking.this,recharge.class);
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
