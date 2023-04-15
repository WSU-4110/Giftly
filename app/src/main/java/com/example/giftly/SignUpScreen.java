package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.giftly.handler.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


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
        editName = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editEmail = findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtLoginInfo = findViewById(R.id.txtLoginInfo);
        editInterests = findViewById(R.id.userInterests);

        //Auto sign in feature
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
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
                if (isSigningUp) {
                    isSigningUp = false;
                    editName.setVisibility(View.GONE);
                    editInterests.setVisibility(View.GONE);
                    btnSubmit.setText("Log in");
                    txtLoginInfo.setText("Don't have an account? Sign up");
                } else {
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
                if (editEmail.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()) {
                    if (isSigningUp && editName.getText().toString().isEmpty()) {
                        Toast.makeText(SignUpScreen.this, "Empty field(s) detected", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(SignUpScreen.this, "Empty field(s) detected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (isSigningUp) {
                    handleSignUp();
                } else {
                    handleLogin();
                }
            }
        });
    }

    //Signing in exceptions and configurations
    private void handleSignUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString().trim(), editPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Turn the interests box editable object into an arraylist using funky town
                    ArrayList<String> interests = new ArrayList<>(Arrays.asList(editInterests.getText().toString().split("\\n")));
                    //Create a user object from info
                    User user = new User(editName.getText().toString(), interests, new ArrayList<>(0));
                    Log.d(TAG, String.format("Attempting to make new user: %s with uID: %s", user, client.getAuth().getUid()));
                    Futures.addCallback(
                            client.createProfile(user), new FutureCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                                    runOnUiThread(new makeToast("Successfully made Profile"));
                                }

                                @Override
                                public void onFailure(@NonNull Throwable t) {
                                    Log.d(TAG, t.toString());
                                    runOnUiThread(new makeToast(t.toString()));
                                }
                            }, Giftly.service

                    );
                } else {
                    Toast.makeText(SignUpScreen.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Logging in exception and configurations
    private void handleLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignUpScreen.this, HomeScreen.class));
                    Toast.makeText(SignUpScreen.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpScreen.this, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
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

    private class makeToast implements Runnable {
        String message;

        public makeToast(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            Toast.makeText(SignUpScreen.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}