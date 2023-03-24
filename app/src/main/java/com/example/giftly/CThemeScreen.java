package com.example.giftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CThemeScreen extends AppCompatActivity {

    //This will overwrite the settings for the background colors for all screens
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctheme_screen);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(CThemeScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Create variables to connect to xml android:id fields
        Button dftThemeBtn = findViewById(R.id.defaultTheme);
        Button glyThemeBtn = findViewById(R.id.giftlyTheme);
        Button vltThemeBtn = findViewById(R.id.valentinesTheme);
        Button etrThemeBtn = findViewById(R.id.easterTheme);
        Button savingit = findViewById(R.id.saveIt);

        //Save button will save the settings and transition back to the home screen
        savingit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CThemeScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        //The default theme configuration
        dftThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Default_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //Theme color 1 configuration
        glyThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Giftly_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //Theme color 2 configuration
        vltThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Valentines_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //Theme color 3 configuration
        etrThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Easter_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //The action bar will display the back button in the header
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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