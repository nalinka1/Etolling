package com.heshanexample.etolling;

import com.google.gson.annotations.SerializedName;

public class getUpdate {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("revisionNo")
    private int revisionNo;

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
    @SerializedName("balance")
    private float balance;



    public getUpdate(String email, String password, int revisionNo) {
        this.email = email;
        this.password = password;
        this.revisionNo = revisionNo;
    }

    public int getRevisionNo() {
        return revisionNo;
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
    public float getBalance() {
        return balance;
    }
}
