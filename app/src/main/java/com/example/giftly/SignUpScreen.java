package com.example.giftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpScreen extends AppCompatActivity {

    //Create variables for xml id's
    EditText username, password, repassword;
    Button signup, signin;
    DB_Helper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        //Clicking response for the sign up button
        username = (EditText) findViewById(R.id.usernm);
        password = (EditText) findViewById(R.id.userpasswrd);
        repassword = (EditText) findViewById(R.id.userRepasswrd);
        signup = (Button) findViewById(R.id.regisbutton);
        DB = new DB_Helper(this);

        //Back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Clicking response for the sign-up button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.equals("")||pass.equals("")|repass.equals(""))
                    Toast.makeText(SignUpScreen.this,"Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser == false){
                            Boolean insert = DB.insertData(user, pass);
                            if(insert == true){
                                Toast.makeText(SignUpScreen.this,"Register Successful", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                //startActivity(intent);
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
        //});

    }

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