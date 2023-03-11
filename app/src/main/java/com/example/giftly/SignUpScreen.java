package com.example.giftly;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        LoginSignupScreen context = new LoginSignupScreen();
        context.signPress();
        context.buttonPress();
    }
}