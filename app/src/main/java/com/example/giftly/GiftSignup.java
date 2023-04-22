package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.Calendar;

public class GiftSignup extends AppCompatActivity {

    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private RelativeLayout relativeLayout;

    final Calendar myCalendar = Calendar.getInstance();
    EditText editText;

    public void onCreate(Bundle savedInstanceState) {

        popupWindow = null;

        // Call setContentView before super.onCreate
        setContentView(R.layout.activity_gift_signup);
        super.onCreate(savedInstanceState);

        //Enable the back-button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Theme: Fetch the current color of the background
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(GiftSignup.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        TextView giftListDisplay = findViewById(R.id.gift_list);
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
                    public void onFailure(@NonNull Throwable thrown) {
                    }
                }, Giftly.service);

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, textAdd.getText().toString());
                client.setGift(userID, eventID, textAdd.getText().toString());
            }
        });
        Button profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the popup_profile.xml layout
                View popupView = LayoutInflater.from(GiftSignup.this).inflate(R.layout.popup_profile, null);

                // Create a new PopupWindow object
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Set the background drawable of the popup window
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Show the popup window
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });


    }
    /*
    private void showProfilePopup(String profileUserID) {

        relativeLayout = findViewById(R.id.relative_layout);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_profile, null);
        // Find views in the popup layout
        TextView profileName = popupView.findViewById(R.id.profile_name);
        TextView profileEmail = popupView.findViewById(R.id.profile_email);
        TextView bringingItems = popupView.findViewById(R.id.bringing_items);

        // Set profile information
        // Replace with logic to retrieve and set participant information
        String name = "John Doe";
        String email = "johndoe@example.com";
        String items = "Item 1, Item 2, Item 3"; // Replace with your logic to retrieve items
        profileName.setText(name);
        profileEmail.setText(email);
        bringingItems.setText(items);

        // Close button click listener
        popupView.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        // Create the popup window
        popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(GiftSignup.this, android.R.color.transparent)));
        popupWindow.setFocusable(true);

        // Show the popup window only if it is not already showing
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
        }
    }
    */


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