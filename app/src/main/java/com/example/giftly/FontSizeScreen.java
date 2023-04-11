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
import android.widget.SeekBar;
import android.widget.TextView;

public class FontSizeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size_screen);

        //Variables used for saving settings and change preference settings
        Button savingit2 = findViewById(R.id.saveIt2);
        SharedPreferences sharedPreferences;

        //Font size slider implementation
        SeekBar fontSizeSeekBar = findViewById(R.id.fontSizeButton);
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float newSize = progress;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("font_size", newSize);
                editor.apply();
                setFontSize(newSize);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        });

        // Apply the saved font size on app startup
        float savedSize = preferences.getFloat("font_size", 16);
        fontSizeSeekBar.setProgress((int) savedSize);
        setFontSize(savedSize);

        //These 3 code lines will change the font size, while calling the iterateViews methods
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

    //Method used to store the font size
    public void setFontSize(float size) {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        iterateTextViews(rootView, size);
    }

    //Method is used to iterate through the java files to change the font sizes
    private void iterateTextViews(ViewGroup parent, float size) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                iterateTextViews((ViewGroup) child, size);
            } else if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            }
        }
    }

    public int matt(int i, int j) {
        int sum = i + j;
        return sum;
    }
}