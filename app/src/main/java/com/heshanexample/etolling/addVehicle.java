package com.heshanexample.etolling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addVehicle extends AppCompatActivity {
    EditText vehicleNumber;
    EditText myEmail;
    EditText myPassword;
    private String correct_address;
    private String correct_password;
    private String vehicle_No;
    private String vehicle_Class;
    private String enteredEmail;
    private String enteredPassword;

    JsonSignInApi addNewVehicle;
    ArrayList got_classes;
    ArrayAdapter<String> adapter2;
    String MacListString;
    private int mode_id;


    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        //get email and password
        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        correct_address = getDetails.getString("user_email",null);
        correct_password= getDetails.getString("user_password",null);

        //get vehicle class array
        got_classes=getIntent().getStringArrayListExtra("classes");

        // set spinner////////////////////////////////////
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, got_classes);
        Spinner class_drop = (Spinner)findViewById(R.id.vehicle_class);
        class_drop.setAdapter(adapter2);
        class_drop.setSelection(0, true);
        View v = class_drop.getSelectedView();
        //((TextView)v).setTextColor(Color.YELLOW);
        ((TextView)v).setTextSize(18);
        vehicle_Class=got_classes.get(0).toString();

        class_drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle_Class=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // define text view
        vehicleNumber = (EditText)findViewById(R.id.vehicle_number);
        myEmail =(EditText)findViewById(R.id.email_adress);
        myPassword=(EditText)findViewById(R.id.password);

        add = (Button)findViewById(R.id.add_vehicle);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicle_No=vehicleNumber.getText().toString();

                enteredEmail=myEmail.getText().toString();
                enteredPassword=myPassword.getText().toString();
                if(passwordValidation()&& EmailValidation()){
                    AlertDialog.Builder addVehicleConfirmation = new AlertDialog.Builder(addVehicle.this);
                    addVehicleConfirmation.setMessage("\n\t\tVehicle Details\n\n"+"Vehicle Number : "+vehicle_No+"\n"+"vehicle Class     : "+vehicle_Class);
                    addVehicleConfirmation.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            addNewVehicle = retro.create(JsonSignInApi.class);
                            newVehicle vehicle = new newVehicle(vehicle_No,vehicle_Class,enteredEmail,enteredPassword);
                            Call<newVehicle> call = addNewVehicle.addVehicle(vehicle);
                            call.enqueue(new Callback<newVehicle>() {
                                @Override
                                public void onResponse(Call<newVehicle> call, Response<newVehicle> response) {
                                    if(!response.isSuccessful()){
                                        return;
                                    }
                                    newVehicle vehicle= response.body();
                                    Intent goHome = new Intent(addVehicle.this,updateData.class);
                                    goHome.putExtra("check_connection",0);
                                    goHome.putExtra("macAddressListB",MacListString);
                                    goHome.putExtra("mode",mode_id);
                                    startActivity(goHome);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<newVehicle> call, Throwable t) {
                                    Intent failed = new Intent(addVehicle.this,InternetFailure.class);
                                    failed.putExtra("check_connection",0);
                                    failed.putExtra("macAddressListB",MacListString);
                                    failed.putExtra("mode",mode_id);
                                    startActivity(failed);
                                    finish();
                                }
                            });

                        }
                    });

                    addVehicleConfirmation.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent goBackToVehicle = new Intent(addVehicle.this,addVehicle.class);
                            startActivity(goBackToVehicle);
                            finish();

                        }
                    });
                    AlertDialog AlertLogout = addVehicleConfirmation.create();
                    AlertLogout.setTitle("Check Vehicle Details !!!");
                    AlertLogout.show();
                }
                else if(passwordValidation()){
                    myEmail.setText("");
                }
                else if(EmailValidation()){
                    myPassword.setText("");
                }
                else {
                    myPassword.setText("");
                    myEmail.setText("");
                }
            }
        });
    }
    private boolean EmailValidation(){
        String emailInput = myEmail.getEditableText().toString().trim();
        if(emailInput.isEmpty()){
            myEmail.setError("Field can't be empty");
            return false;
        }
        else if(!correct_address.equals(enteredEmail)&&!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            myEmail.setError("Please Enter correct Email Address");
            return false;
        }
        else {
            myEmail.setError(null);
            return true;
        }

    }
    //password validation
    private boolean passwordValidation(){
        String passwordInput= myPassword.getEditableText().toString();
        if(passwordInput.isEmpty()){
            myPassword.setError("Field can't be empty");
            return false;
        }
        else if(!enteredPassword.equals(correct_password)){
            myPassword.setError("Please Enter correct Password");
            return false;
        }
        else{
            myPassword.setError(null);
            return true;
        }
    }

}
