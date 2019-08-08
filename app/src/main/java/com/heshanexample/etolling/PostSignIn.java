package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class PostSignIn {

    @SerializedName("email")
    private String InputEmail;
    @SerializedName("password")
    private String InputPassword;

    public PostSignIn(String inputEmail, String inputPassword) {
        InputEmail = inputEmail;
        InputPassword = inputPassword;
    }


}
