package com.example.giftly;

import android.widget.Toast;

import androidx.annotation.NonNull;

public class no_signinScreen implements SignUpInterface {
    @Override
    public void handleSign(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void handleLogin(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Toast.makeText(SignUpScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
