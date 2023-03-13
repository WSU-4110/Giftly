package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.Calendar;

public class GiftSignup extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText editText;
    private SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gift_signup);
        super.onCreate(savedInstanceState);

        //Enable the back-button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(GiftSignup.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        TextView giftListDisplay = findViewById(R.id.locatiopn_entry);
        Button editEventButton = (Button) findViewById(R.id.button_add_event);
        EditText textAdd = findViewById(R.id.event_name_entry);

        Intent participantIntent = getIntent();
        String eventID = participantIntent.getStringExtra("eventID");
        String userID = participantIntent.getStringExtra("userID");


        Futures.addCallback(
                client.readGiftList(eventID, userID),
                new FutureCallback<String>() {
                    //anon class for runnable
                    class updateGiftList implements Runnable {
                        final String giftList;

                        public updateGiftList(String giftList) {
                            this.giftList = giftList;
                        }

                        @Override
                        public void run() {
                            giftListDisplay.setText(giftList);
                        }
                    }
                    @Override
                    public void onSuccess(String giftList) {
                        Log.d(TAG, "Successfully pulled Gifts");
                        runOnUiThread(new updateGiftList(giftList));
                    }

                    @Override
                    public void onFailure(Throwable thrown) {
                    }
                }, Giftly.service);

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.setGift(userID, eventID, textAdd.getText().toString());
            }
        });
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