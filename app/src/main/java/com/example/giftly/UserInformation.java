package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class UserInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
    }

    public String email(String email) {
        System.out.println("test@gmail.com");
        return email;
    }

    public String username(String username) {
        System.out.println("Test User");
        return username;
    }

    public String phoneNumber(String phoneNumber) {
        System.out.println(phoneNumber);
        return phoneNumber;
    }

    public String address(String address) {
        System.out.println("123 Detroit");
        return address;
    }

    public String Fname(String Fname) {
        System.out.println("Firstname");
        return Fname;
    }

    public String Lname(String Lname) {
        System.out.println("Lastname");
        return Lname;
    }
}