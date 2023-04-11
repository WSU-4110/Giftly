package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class testClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_class);
    }

    public int matt(int i, int j) {
        int sum = i + j;
        return sum;
    }

    public int matt2(int i, int j, int k) {
        int sum = i + j + k;
        return sum;
    }

    public int matt3(int i) {
        int num = i;
        return num;
    }

    public int matt4(int i, int j) {
        int subtract = i - j;
        return subtract;
    }

    public String Fname(String Fname) {
        System.out.println("Fname");
        return Fname;
    }

    public String Lname(String Lname) {
        System.out.println("Lname");
        return Lname;
    }
}