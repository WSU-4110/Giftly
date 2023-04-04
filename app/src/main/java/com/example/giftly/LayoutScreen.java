package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LayoutScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Button layout_1_Btn, layout_2_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_screen);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(LayoutScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        layout_1_Btn = findViewById(R.id.Layout1xml);
        layout_2_Btn = findViewById(R.id.Layout2xml);

        layout_1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = 0;

                Intent intent = new Intent(LayoutScreen.this, HomeScreen.class);
                intent.putExtra("VALUE", value);
                startActivity(intent);
            }
        });

        layout_2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = 1;

                Intent intent = new Intent(LayoutScreen.this, HomeScreen.class);
                intent.putExtra("VALUE", value);
                startActivity(intent);
            }
        });


    }




}