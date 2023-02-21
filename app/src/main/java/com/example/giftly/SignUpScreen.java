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
    private EditText editName, editPassword, editEmail, editInterests;
    private Button btnSubmit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView txtLoginInfo;
    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //Write the feed onto real-time
        //Real-time database management
        /*
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(SignUpScreen.this, HomeScreen.class);
                startActivity(intent);
                finish();
                return;
            }
        };*/
        
        editName = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);
        editInterests = (EditText) findViewById(R.id.userInterests);

        //Auto sign in if the user hasn't logged out
        /*
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
            finish();
        }*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        txtLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSigningUp) {
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
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

    }

    //Signing in exceptions and configurations
    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //After signing up, transition to the home screen

                    //client.createProfile(new User());

                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                    Toast.makeText(SignUpScreen.this, "Signed in successfully", Toast.LENGTH_SHORT).show();

                    //Real-time database management
                    /*
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    String name = editName.getText().toString();
                    String interests = editInterests.getText().toString();
                    Map newPost = new HashMap();
                    newPost.put("name", name);
                    newPost.put("interests",interests);
                    current_user_db.setValue(newPost);*/
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
                    //After logging in, transition to the home screen
                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                    Toast.makeText(SignUpScreen.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Back button configuration
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