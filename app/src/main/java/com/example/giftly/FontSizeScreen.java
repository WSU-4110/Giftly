package com.example.giftly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontSizeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size_screen);

        //Create variables to connect to xml android:id fields
        Button fontSizeButton = findViewById(R.id.fontSizeButton);
        Button fontSizeButton2 = findViewById(R.id.fontSizeButton2);
        Button fontSizeButton3 = findViewById(R.id.fontSizeButton3);
        Button savingit2 = findViewById(R.id.saveIt2);
        SharedPreferences sharedPreferences;

        //Functionality to set the font size with a value of 16
        fontSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newSize = 16; // set the new font size
                SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("font_size", newSize);
                editor.apply();
                ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
                iterateViews(rootView, newSize);
            }
        });

        //Functionality to set the font size with a value of 20
        fontSizeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newSize = 20; // set the new font size
                SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("font_size", newSize);
                editor.apply();
                ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
                iterateViews(rootView, newSize);
            }
        });

        //Functionality to set the font size with a value of 24
        fontSizeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newSize = 24; // set the new font size
                SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("font_size", newSize);
                editor.apply();
                ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
                iterateViews(rootView, newSize);
            }
        });

        //These 4 code lines will change the font size, while calling the iterateViews methods
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        float fontSize = preferences.getFloat("font_size", getResources().getDimension(R.dimen.fab_margin));
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        iterateViews(rootView, fontSize);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(FontSizeScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Save button will save the settings and transition back to the home screen
        savingit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FontSizeScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        //The action bar will display the back button in the header
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //This will loop through Button and EditText fields in the xml files to change the text sizes
    private void iterateViews(View view, float size) {
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