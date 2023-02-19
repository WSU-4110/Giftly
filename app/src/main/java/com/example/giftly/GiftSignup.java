package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;
import static com.example.giftly.Giftly.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.Calendar;

public class GiftSignup extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText editText;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gift_signup);
        super.onCreate(savedInstanceState);

        TextView giftListDisplay = findViewById(R.id.gift_list);
        Button editEventButton = findViewById(R.id.button_add_gift);
        EditText textAdd = findViewById(R.id.add_gift);

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
}