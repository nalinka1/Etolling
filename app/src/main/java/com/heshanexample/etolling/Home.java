package com.heshanexample.etolling;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView userNameShow,userEmailShow;
    Intent main_activity;
    String user_name,user_email;
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



        Toast.makeText(this, " welcome to home Page", Toast.LENGTH_LONG).show();

        // get Navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);

// get header
        View navHeader = navigationView.getHeaderView(0);
        userNameShow = (TextView) navHeader.findViewById(R.id.UserNameTextView);
        userEmailShow=(TextView)navHeader.findViewById(R.id.UserEmailTextView);

        main_activity=getIntent();
        user_name = main_activity.getStringExtra("user");
        user_email = main_activity.getStringExtra("Email");

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
        } else if (id == R.id.nav_History) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_Settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_contactUs) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}