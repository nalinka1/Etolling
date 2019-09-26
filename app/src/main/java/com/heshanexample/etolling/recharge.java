package com.heshanexample.etolling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class recharge extends AppCompatActivity {
    EditText myPin;
    EditText myEmail;
    EditText myPassword;
    TextView currentBalance;
    Button add;

    private String correct_address;
    private String correct_password;
    private float balance;
    private float newBalance;
    private String enteredEmail;
    private String enteredPassword;
    private String pin;
    private String MacListString;

    JsonSignInApi setPinJSon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            MacListString = extras.getString("macAddressListB");
        }


        myPin = (EditText)findViewById(R.id.getPin);
        myEmail =(EditText)findViewById(R.id.getEmail);
        myPassword=(EditText)findViewById(R.id.getPassword);
        currentBalance=(TextView)findViewById(R.id.currentBalance);

        add = (Button)findViewById(R.id.submit_topup);

        SharedPreferences getDetails = getSharedPreferences("UserData",0);
        correct_address = getDetails.getString("user_email",null);
        correct_password= getDetails.getString("user_password",null);
        balance = getDetails.getFloat("balance",0);

        currentBalance.setText(Double.toString(balance));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredEmail=myEmail.getText().toString();
                enteredPassword=myPassword.getText().toString();
                pin = myPin.getText().toString();
                if(EmailValidation()&& passwordValidation()){
                    AlertDialog.Builder addPinConfirmation = new AlertDialog.Builder(recharge.this);
                    addPinConfirmation.setMessage("\n\t\tPin Number : "+pin);
                    addPinConfirmation.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            setPinJSon = retro.create(JsonSignInApi.class);
                            topup newTopup = new topup(pin,correct_address,correct_password);
                            Call<topup> call = setPinJSon.addPin(newTopup);
                            call.enqueue(new Callback<topup>() {
                                @Override
                                public void onResponse(Call<topup> call, Response<topup> response) {
                                    if(!response.isSuccessful()){
                                        Intent submitAgain = new Intent(recharge.this,recharge.class);
                                        startActivity(submitAgain);
                                        return;
                                    }
                                    topup tp = response.body();
                                    newBalance= tp.getBalance();
                                    SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                                    SharedPreferences.Editor edit = storeInput.edit();
                                    edit.putFloat("balance",newBalance);
                                    edit.commit();

                                    Intent goBackHome =  new Intent(recharge.this,Home.class);
                                    startActivity(goBackHome);
                                    finish();



                                }

                                @Override
                                public void onFailure(Call<topup> call, Throwable t) {
                                        Intent checkConnection = new Intent(recharge.this,updateData.class);
                                        checkConnection.putExtra("macAddressListB",MacListString);
                                        startActivity(checkConnection);
                                        finish();
                                }
                            });

                        }
                    });

                    addPinConfirmation.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent rechargeMe = new Intent(recharge.this,recharge.class);
                            startActivity(rechargeMe);
                            finish();

                        }
                    });
                    AlertDialog AlertLogout = addPinConfirmation.create();
                    AlertLogout.setTitle("Check Pin Number !!!");
                    AlertLogout.show();

                }else if(passwordValidation()){
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
