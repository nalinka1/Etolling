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
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup extends AppCompatActivity implements View.OnClickListener{

    Button signUpButton,forgetButton,registerButton;
    private String password;
    protected String emailAddress;
    EditText getEmail,getPassword;
    boolean emailCorrect,passwordCorrect;
    boolean serverBusy;




    // https variables
    JsonSignInApi jsonSignInApi;

    // temporary name-- delete this after testing is finished
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


                // https validation.....................................................................................................................................................



                //................................................................................................................................
                // delete this later

                emailCorrect=true;
                passwordCorrect=true;
                serverBusy=false;


                //......................................................................................................................................................................

                if(passwordValidation()&& EmailValidation()){

                    Retrofit retro = new Retrofit.Builder().baseUrl("https://api.myjson.com/bins/").addConverterFactory(GsonConverterFactory.create())
                            .build();

                    jsonSignInApi = retro.create(JsonSignInApi.class);


                    PostSignIn post = new PostSignIn(emailAddress,password);
                    Call<PostSignIn> call = jsonSignInApi.createPost(post);



                    call.enqueue(new Callback<PostSignIn>() {
                        @Override
                        public void onResponse(Call<PostSignIn> call, Response<PostSignIn> response) {
                            if(!response.isSuccessful()){
                                Toast.makeText(getBaseContext(),"Server is busy",Toast.LENGTH_LONG).show();
                                getEmail.setText("code : "+response.code());

                                //uncomment this later

                                //return;

                            }

                            int code = response.code();
                            //uncomment this later
                            //if(code==302){
                            if(true){
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
                            else if(code==403){
                                getPassword.getText().clear();
                                passwordValidation();
                            }
                            else if (code==404){
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

                        @Override
                        public void onFailure(Call<PostSignIn> call, Throwable t) {
                            getEmail.setText("Internet failure");

                        }
                    });

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

    // sending info encription
    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }
    //sending info decrption
    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }
    //email validation
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
    //password validation
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

    // https function post request

    protected void createPost(String inputEmail,String inputPassword){

        Retrofit retro = new Retrofit.Builder().baseUrl("https://api.myjson.com/bins/").addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonSignInApi = retro.create(JsonSignInApi.class);

        PostSignIn post = new PostSignIn(inputEmail,inputPassword);
        Call<PostSignIn> call = jsonSignInApi.createPost(post);


        call.enqueue(new Callback<PostSignIn>() {
            @Override
            public void onResponse(Call<PostSignIn> call, Response<PostSignIn> response) {
                if(!response.isSuccessful()){

                    getEmail.setText("code : "+response.code());
                    return;

                }

            }

            @Override
            public void onFailure(Call<PostSignIn> call, Throwable t) {
                getEmail.setText(t.getMessage());
            }
        });

    }



}
