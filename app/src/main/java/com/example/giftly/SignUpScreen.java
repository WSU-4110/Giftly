package com.example.giftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giftly.handler.FireBaseClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpScreen extends AppCompatActivity {
    private EditText editUsername, editPassword, editEmail;
    private Button btnSubmit;
    private TextView txtLoginInfo;
    DB_Helper DB;
    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);
        DB = new DB_Helper(this);

        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp) {
                    isSigningUp = false;
                    //Hide the visibility of the text
                    editUsername.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Dont have an account? Sign up");
                }else{
                    isSigningUp = true;
                    editUsername.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Sign up");
                    txtLoginInfo.setText("Already have an account? Log in");
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editEmail.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
                    if(isSigningUp && editUsername.getText().toString().isEmpty()){
                        Toast.makeText(SignUpScreen.this, "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(isSigningUp){
                    handleSignUp();
                }else{
                    handleLogin();
                }
            }
        });

    }

    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpScreen.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpScreen.this, FireBaseClient.getCurrentUserName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpScreen.this, HomeScreen.class);
                    startActivity(i);

                }else{
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}