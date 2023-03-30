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

import com.google.firebase.auth.FirebaseAuth;

public class SettingsScreen extends AppCompatActivity {
    //Create variables to connect to xml android:id fields
    private Button btnLogOut;
    public Button choosingThemeBtn, choosingFontSizeBtn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        //These 4 code lines will change the font size while calling iterateViews
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        float fontSize = preferences.getFloat("font_size", getResources().getDimension(R.dimen.fab_margin));
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        iterateViews(rootView, fontSize);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(SettingsScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Connect the variables with the xml android:ids
        btnLogOut = findViewById(R.id.button_log_out);
        choosingThemeBtn = findViewById(R.id.themeBtn);
        choosingFontSizeBtn = findViewById(R.id.fontSizeBtn);

        //Creating a button response for themes
        choosingThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsScreen.this, CThemeScreen.class);
                startActivity(intent);
            }
        });

        //Creating a button response for font size
        choosingFontSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsScreen.this, FontSizeScreen.class);
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
}