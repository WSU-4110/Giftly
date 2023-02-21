package com.example.giftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsScreen extends AppCompatActivity {
    private Button btnLogOut;
    public Button choosingThemeBtn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(SettingsScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        btnLogOut = findViewById(R.id.button_log_out);
        choosingThemeBtn = findViewById(R.id.themeBtn);

        choosingThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsScreen.this, CThemeScreen.class);
                startActivity(intent);
            }
        });

        //Log out button configuration
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        //Enable the back-button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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