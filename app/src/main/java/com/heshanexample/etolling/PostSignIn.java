package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class PostSignIn {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("address")
    private String address;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("idNumber")
    private String idNumber;
    @SerializedName("account")
    private Object account;
    @SerializedName("image")
    private String image;
    @SerializedName("revisionNo")
    private int revisionNo;


    public PostSignIn(String inputEmail, String inputPassword) {
        email = inputEmail;
        password = inputPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public Object getAccount() {
        return account;
    }

    public String getImage() {
        return image;
    }

    public int getRevisionNo() {
        return revisionNo;
    }
}
