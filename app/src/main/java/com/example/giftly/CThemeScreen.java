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

import java.util.ArrayList;
import java.util.List;


//private SharedPreferences sharedPreferences;

public class CThemeScreen extends AppCompatActivity implements ThemeObserver {
        private ThemeSubject themeSubject;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ctheme_screen);

            themeSubject = new ThemeSubject();
            themeSubject.registerObserver(this);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(CThemeScreen.this, R.color.Default_color));
            getWindow().getDecorView().setBackgroundColor(savedColor);

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
                    themeSubject.notifyObservers(selectedColor);
                }
            });

            glyThemeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Giftly_color);
                    sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                    themeSubject.notifyObservers(selectedColor);
                }
            });
            //Valentines
            vltThemeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Valentines_color);
                    sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                    themeSubject.notifyObservers(selectedColor);
                }
            });

            etrThemeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Easter_color);
                    sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                    themeSubject.notifyObservers(selectedColor);
                }
            });
        }

        @Override
        public void onThemeChanged(int newColor) {
            getWindow().getDecorView().setBackgroundColor(newColor);
        }
}


    //Back button
    //ActionBar actionBar = getSupportActionBar();
    //actionBar.setDisplayHomeAsUpEnabled(true);



    //Back button configuration
   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
}
*/