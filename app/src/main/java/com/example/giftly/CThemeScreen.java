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
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctheme_screen);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(CThemeScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Create theme buttons
        Button dftThemeBtn = findViewById(R.id.defaultTheme);
        Button glyThemeBtn = findViewById(R.id.giftlyTheme);
        Button vltThemeBtn = findViewById(R.id.valentinesTheme);
        Button etrThemeBtn = findViewById(R.id.easterTheme);
        Button savingit = findViewById(R.id.saveIt);

        savingit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CThemeScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        dftThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Default_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        glyThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Giftly_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        vltThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Valentines_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        etrThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Easter_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });



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