package com.heshanexample.etolling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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


    // https variables
    JsonSignInApi jsonSignInApi;

    // https response for sign In details variables

    private String userFirstName;
    private String userLastName;
    private String userAddress;
    private String userIdNumber;
    private String userPhoneNumber;
    private String accountNumber;
    private String ownerName;
    private int balance;

    private String userName;
    private int revesionNumber;



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


                //......................................................................................................................................................................

                if(passwordValidation()&& EmailValidation()){
                    Toast.makeText(getBaseContext(),"User Data Downloading...",Toast.LENGTH_LONG).show();

                    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();
                    Retrofit retro = new Retrofit.Builder().baseUrl("https://open-road-tolling.herokuapp.com/api/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                            .build();

                    jsonSignInApi = retro.create(JsonSignInApi.class);


                    final String encriptPassword = encrypt(password);
                    final String encriptEmail    = encrypt(emailAddress);

                    PostSignIn post = new PostSignIn(emailAddress,password);
                    //........................uncomment this..later..............
                    Call<PostSignIn> call = jsonSignInApi.createPost(post);



                    call.enqueue(new Callback<PostSignIn>() {
                        @Override
                        public void onResponse(Call<PostSignIn> call, Response<PostSignIn> response) {
                            if(!response.isSuccessful()){
                                int code = response.code();
                                getEmail.setText("response: "+code);
                                if(code==403){
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
                                return;

                            }



                            ObjectMapper mapper = new ObjectMapper();
                            PostSignIn signIndetails = response.body();

                            //Log.d("tag",signIndetails.toString());

                            userFirstName = signIndetails.getFirstName();
                            userLastName= signIndetails.getLastName();
                            userAddress= signIndetails.getAddress();
                            userIdNumber=signIndetails.getIdNumber();
                            userPhoneNumber=signIndetails.getPhoneNumber();
                            revesionNumber= signIndetails.getRevisionNo();

                            userName = userFirstName+" "+userLastName;

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

                            getEmail.getText().clear();
                            getPassword.getText().clear();

                            SharedPreferences storeInput = getApplicationContext().getSharedPreferences("UserData",0);
                            SharedPreferences.Editor edit = storeInput.edit();
                            edit.putString("user_password",password);
                            edit.putString("user_email",emailAddress);
                            edit.putString("first_name",userFirstName);
                            edit.putString("last_name",userLastName);
                            edit.putString("id_number",userIdNumber);
                            edit.putString("phone_number",userPhoneNumber);
                            edit.putString("address",userAddress);
                            edit.putString("account_number",accountNumber);
                            edit.putString("owner_name",ownerName);
                            edit.putInt("balance",balance);
                            edit.putString("encoded_image",pureIm);
                            edit.putString("user_name",userName);
                            edit.putInt("revision_number",revesionNumber);

                            edit.commit();


                            Intent goHome = new Intent(Signup.this,Home.class);
                            goHome.putExtra("user",userName);
                            goHome.putExtra("Email",emailAddress);
                            startActivity(goHome);
                            finish();


                        }

                        @Override
                        public void onFailure(Call<PostSignIn> call, Throwable t) {
                            Toast.makeText(getBaseContext(),"Internet failure.!",Toast.LENGTH_LONG).show();
                            getEmail.setText("Internet failure");

                        }
                    });

                }


                break;
            case R.id.Register:

                Intent RegisterMe = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open-road-tolling.herokuapp.com/register"));
                startActivity(RegisterMe);
                break;
            case R.id.ForgetPassword:

                Intent resetPassword = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open-road-tolling.herokuapp.com/register"));
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
    



}
