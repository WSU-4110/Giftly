package com.example.giftly;

import static com.example.giftly.Giftly.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giftly.handler.FireBaseClient;
import com.example.giftly.handler.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignUpScreen extends AppCompatActivity {

    //Create variables to connect to xml android:id fields
    private EditText editName, editPassword, editEmail, editInterests;
    private Button btnSubmit;
    private TextView txtLoginInfo;
    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //Connect the variables with the xml android:ids
        editName = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);
        editInterests = (EditText) findViewById(R.id.userInterests);

        //Auto sign in feaure
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
            finish();
        }

        //The action bar will display the back button in the header
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //This will change the screen between log in/sign up
        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp) {
                    isSigningUp = false;
                    editName.setVisibility(View.GONE);
                    editInterests.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Don't have an account? Sign up");
                }else{
                    isSigningUp = true;
                    editName.setVisibility(View.VISIBLE);
                    editInterests.setVisibility(View.VISIBLE);
                    btnSubmit.setText("Sign up");
                    txtLoginInfo.setText("Already have an account? Log in");
                }
            }
        });

        //When the user presses the submit button, execute handleSignUp() or handleLogin() methods
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editEmail.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
                    if(isSigningUp && editName.getText().toString().isEmpty()){
                        Toast.makeText(SignUpScreen.this, "Empty field(s) detected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        Toast.makeText(SignUpScreen.this, "Empty field(s) detected", Toast.LENGTH_SHORT).show();
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

    //Signing in exceptions and configurations
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString().trim(),editPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

    //Logging in exception and configurations
    private void handleLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                    Toast.makeText(SignUpScreen.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Back button configuration that allows the feature to go back to the previous screen
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}