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
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


//private SharedPreferences sharedPreferences;

public class ThemeScreen extends AppCompatActivity implements ThemeObserver {
        private ThemeSubject themeSubject;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.theme_screen);

            themeSubject = new ThemeSubject();
            themeSubject.registerObserver(this);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(ThemeScreen.this, R.color.Default_color));
            getWindow().getDecorView().setBackgroundColor(savedColor);

            Button dftThemeBtn = findViewById(R.id.defaultTheme);
            Button lightMode = findViewById(R.id.lightTheme);
            Button savingit = findViewById(R.id.saveIt);

            SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
            float fontSize = preferences.getFloat("font_size", getResources().getDimension(R.dimen.fab_margin));
            ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
            iterateViews(rootView, fontSize);
            savingit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ThemeScreen.this, HomeScreen.class);
                    startActivity(intent);
                }
            });

            dftThemeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedColor = ContextCompat.getColor(ThemeScreen.this, R.color.Default_color);
                    sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                    themeSubject.notifyObservers(selectedColor);
                }
            });

            lightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedColor = ContextCompat.getColor(ThemeScreen.this, R.color.Light_color);
                    sharedPreferences.edit().putInt("BackgroundColor", selectedColor).apply();
                    themeSubject.notifyObservers(selectedColor);
                }
            });

            //The action bar will display the back button in the header
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        @Override
        public void onThemeChanged(int newColor) {
            getWindow().getDecorView().setBackgroundColor(newColor);
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

    void iterateViews(View view, float size) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                iterateViews(viewGroup.getChildAt(i), size);
            }
        } else if (view instanceof TextView || view instanceof Button || view instanceof EditText) {
            TextView textView = (TextView) view;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }
}


