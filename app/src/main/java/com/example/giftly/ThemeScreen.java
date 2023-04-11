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

public class ThemeScreen extends AppCompatActivity {

    //This will overwrite the settings for the background colors for all screens
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_screen);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(ThemeScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Create variables to connect to xml android:id fields
        Button defaultThemeButton = findViewById(R.id.defaultTheme);
        Button color1ThemeButton = findViewById(R.id.giftlyTheme);
        Button color2ThemeButton = findViewById(R.id.valentinesTheme);
        Button color3ThemeButton = findViewById(R.id.easterTheme);
        Button saveButton = findViewById(R.id.saveIt);

        //Save button will save the settings and transition back to the home screen
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThemeScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        //The default theme configuration
        defaultThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(ThemeScreen.this, R.color.Default_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //Theme color 1 configuration
        color1ThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(ThemeScreen.this, R.color.Giftly_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //Theme color 2 configuration
        color2ThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(ThemeScreen.this, R.color.Valentines_color);
                sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                getWindow().getDecorView().setBackgroundColor(selectedColor);
            }
        });

        //Theme color 3 configuration
        color3ThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedColor = ContextCompat.getColor(ThemeScreen.this, R.color.Easter_color);
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