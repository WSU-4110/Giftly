package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//private SharedPreferences sharedPreferences;

public class CThemeScreen extends AppCompatActivity implements ThemeObserver {
        private ThemeSubject themeSubject;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.theme_screen);

            themeSubject = new ThemeSubject();
            themeSubject.registerObserver(this);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(CThemeScreen.this, R.color.Default_color));
            getWindow().getDecorView().setBackgroundColor(savedColor);

            Button dftThemeBtn = findViewById(R.id.defaultTheme);
            Button lightMode = findViewById(R.id.lightTheme);
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

            lightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedColor = ContextCompat.getColor(CThemeScreen.this, R.color.Light_color);
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


