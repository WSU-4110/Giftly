package com.example.giftly;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public interface SignUpInterface {
    public void handleSignUp();
    public void handleLogin();
    public void textLoginInfo();
    public void buttonSubmit();
}

public class signinScreen implements SignUpInterface {
    private EditText editName, editPassword, editEmail, editInterests;
    private Button btnSubmit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView txtLoginInfo;
    private boolean isSigningUp = true;
    @Override
    public void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                    Toast.makeText(SignUpScreen.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void handleLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //After logging in, transition to the home screen
                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                    Toast.makeText(SignUpScreen.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void textLoginInfo(){
        if(isSigningUp){
            isSigningUp = false;
            //Hide the visibility of the text
            editName.setVisibility(View.GONE);
            editInterests.setVisibility(View.GONE);
            btnSubmit.setText("Log in");
            txtLoginInfo.setText("Dont have an account? Sign up");
        }else{
            isSigningUp = true;
            editName.setVisibility(View.VISIBLE);
            editInterests.setVisibility(View.VISIBLE);
            btnSubmit.setText("Sign up");
            txtLoginInfo.setText("Already have an account? Log in");
        }
    }

    @Override
    public void buttonSubmit(){
        if(editEmail.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
            if(isSigningUp && editName.getText().toString().isEmpty()){
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
}
