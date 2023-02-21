package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditGiftListIdeas extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        button = (Button) findViewById(R.id.themeBtn);
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openGiftIdeas();
           }
        });
    }

    public void openGiftIdeas() {
        Intent intent = new Intent(this, EditGiftListIdeas.class);
        startActivity(intent);
    }
}