package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Create Button Variables for log-in and sign-up
    public Button button_signup, button_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect Button values with xml ids'
        button_signup = (Button) findViewById(R.id.signupbtn);
        button_signin = (Button) findViewById(R.id.signinbtn);

        //Clicking response for the sign up button
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpScreen.class);
                startActivity(intent);
            }
        });

        //Clicking response for the sign in button
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
            }
        });
    }
}