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

public class SignUpScreen extends AppCompatActivity {

    //Declaring the ID views from the xml file
    private EditText editUsername, editPassword, editEmail;
    private Button btnSubmit;
    private TextView txtLoginInfo;
    //Button signup, signin;
    //DB_Helper DB;

    //Determine if the user successfully login in or not
    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //Initialize the variables declared
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);

        //Clicking response for the initializers
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
                    //handleSignUp(); ======firebase connectivity=======
                }else{
                    //handleLogin(); =======firebase connectivity======
                }
            }
        });

        //Clicking response for the sign up button
        //editUsername = (EditText) findViewById(R.id.editUsername);
        //editPassword = (EditText) findViewById(R.id.editPassword);
        //editEmail = (EditText) findViewById(R.id.editEmail);
        //signup = (Button) findViewById(R.id.btnSubmit);
        //DB = new DB_Helper(this);

        //Back button
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        //Clicking response for the sign-up button
        /*
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editUsername.getText().toString();
                String pass = editPassword.getText().toString();
                String repass = editEmail.getText().toString();

                if(user.equals("")||pass.equals("")|repass.equals(""))
                    Toast.makeText(SignUpScreen.this,"Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser == false){
                            Boolean insert = DB.insertData(user, pass);
                            if(insert == true){
                                Toast.makeText(SignUpScreen.this,"Register Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpScreen.this,"Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(SignUpScreen.this,"User already exists! Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignUpScreen.this,"Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //Clicking response for the sign-in button
        //signin.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View view) {
        //Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
        //startActivity(intent);
        //}
        //});*/

    }

    //This code will be used to connect with firebase after login/signup
    /*
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new onCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpScreen.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString()).addOnCompleteListener(new onCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpScreen.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/







    /*
    //This is the backbutton
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/



}