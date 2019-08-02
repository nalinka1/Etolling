package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup extends AppCompatActivity implements View.OnClickListener{

    Button signUpButton,forgetButton,registerButton;
    private String password;
    protected String emailAddress;
    EditText getEmail,getPassword;
    boolean emailCorrect,passwordCorrect;


    String UserNameForNow= "heshan Nalinka";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        signUpButton= (Button)findViewById(R.id.InputAll);
        forgetButton= (Button)findViewById(R.id.ForgetPassword);
        registerButton= (Button)findViewById(R.id.Register);

        getEmail=(EditText)findViewById(R.id.InputUser);
        getPassword=(EditText)findViewById(R.id.InputPassword);


        signUpButton.setOnClickListener(this);
        forgetButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //mannualy assign the values to emailcorrect and password correct

        emailCorrect= true;
        passwordCorrect=true;

        getEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EmailValidation();
                }
            }
        });

        getPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    passwordValidation();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.InputAll:

                emailAddress= getEmail.getText().toString();
                password= getPassword.getText().toString();

                if(passwordValidation()&& EmailValidation()){
                    if(emailCorrect && passwordCorrect){
                        String encriptPassword = encrypt(password);
                        String encriptEmail    = encrypt(emailAddress);

                        getEmail.getText().clear();
                        getPassword.getText().clear();

                        SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                        SharedPreferences.Editor edit = storeInput.edit();
                        edit.putString("user_password",encriptPassword);
                        edit.putString("user_email",encriptEmail);

                        edit.commit();


                        Intent goHome = new Intent(Signup.this,Home.class);
                        goHome.putExtra("user",UserNameForNow);
                        goHome.putExtra("Email",emailAddress);
                        startActivity(goHome);
                        finish();
                    }

                    else if(emailCorrect){
                        getPassword.getText().clear();
                        passwordValidation();
                    }
                    else if(passwordCorrect){
                        getEmail.getText().clear();
                        EmailValidation();
                    }
                    else{
                        getEmail.getText().clear();
                        getPassword.getText().clear();
                        passwordValidation();
                        EmailValidation();
                    }
                }


                break;
            case R.id.Register:

                Intent RegisterMe = new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/questions/4396376/how-to-get-edittext-value-and-display-it-on-screen-through-textview/4396400"));
                startActivity(RegisterMe);
                break;
            case R.id.ForgetPassword:

                Intent resetPassword = new Intent(Intent.ACTION_VIEW, Uri.parse("https://stackoverflow.com/questions/4396376/how-to-get-edittext-value-and-display-it-on-screen-through-textview/4396400"));
                startActivity(resetPassword);
                break;
        }
    }
    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
    private boolean EmailValidation(){
        String emailInput = getEmail.getEditableText().toString().trim();
        if(emailInput.isEmpty()){
            getEmail.setError("Field can't be empty");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            getEmail.setError("Please Enter Valid Email Address");
            return false;
        }
        else {
            getEmail.setError(null);
            return true;
        }

    }
    private boolean passwordValidation(){
        String passwordInput= getPassword.getEditableText().toString();
        if(passwordInput.isEmpty()){
            getPassword.setError("Field can't be empty");
            return false;
        }
        else {
            getPassword.setError(null);
            return true;
        }
    }
}
