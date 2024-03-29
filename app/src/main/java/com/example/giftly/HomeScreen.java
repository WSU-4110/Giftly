package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.giftly.handler.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class HomeScreen extends AppCompatActivity {
    public Button settingsBtn;
    public Button addEventBtn;
    public Button joinEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //find Users name and display it to test readUser data
        TextView display = findViewById(R.id.userNameDispl);
        settingsBtn = (Button) findViewById(R.id.SettingsBtn);
        settingsBtn.setBackgroundColor(Color.TRANSPARENT);
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        joinEventBtn = (Button)findViewById(R.id.joinEvent);
        CardView cardView = findViewById(R.id.cardList);

            //Pop up when clicking join event
            joinEventBtn.setOnClickListener(view -> {
                View dialogView = LayoutInflater.from(HomeScreen.this).inflate(R.layout.join_event_dialog, null);
                EditText input = dialogView.findViewById(R.id.event_id_input);

                new AlertDialog.Builder(HomeScreen.this)
                        .setView(dialogView)
                        .setPositiveButton("Join", (dialog, which) -> {
                            String eventId = input.getText().toString();
                            //Create Async Future
                            Futures.addCallback(
                                    client.joinEvent(eventId),
                                    new FutureCallback<>() {

                                        @Override
                                        public void onSuccess(String result) {
                                            Log.d(TAG, "Successfully joined event");

                                            runOnUiThread(() -> {
                                                Toast.makeText(HomeScreen.this, result, Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition(0, 0);
                                            });

                                        }

                                        @Override
                                        public void onFailure(@NonNull Throwable thrown) {

                                            String message = (Objects.requireNonNull(thrown.getMessage()).isEmpty() ? "There was a problem joining the Event" : thrown.getMessage());

                                            runOnUiThread(() -> Toast.makeText(HomeScreen.this, message, Toast.LENGTH_SHORT).show());
                                            Log.d(TAG, thrown.toString());
                                        }
                                    }, Giftly.service);

                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                        .show();
            });


            settingsBtn.setOnClickListener(view -> {
                Intent intent = new Intent(HomeScreen.this, SettingsScreen.class);
                startActivity(intent);
            });


        addEventBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, AddEventScreen.class);
            startActivity(intent);
        });

        //This call will display the user's name in the greeting message
        Futures.addCallback(
                Giftly.client.readUser(client.getAuth().getUid()),
                new FutureCallback<>() {
                    @Override
                    public void onSuccess(User result) {
                        display.setText(result.getFullName());
                        //This call will display the user's name in the greeting message
                        Futures.addCallback(
                                Giftly.client.readEvent(result.getEvents()),
                                new FutureCallback<>() {
                                    @Override
                                    public void onSuccess(ArrayList<IEvent> events) {
                                        //anon class for updating GUI thread
                                        class updateEvents implements Runnable {
                                            final ArrayList<IEvent> events;

                                            updateEvents(ArrayList<IEvent> e) {
                                                events = e;
                                            }

                                            @Override
                                            public void run() {
                                                LinearLayout eventList = findViewById(R.id.events);
                                                Log.d(TAG, "Adding Events to List:");

                                                TextView header = new TextView(eventList.getContext());
                                                header.setText(R.string.ongoing_events);
                                                header.setTextSize(20);
                                                header.setPadding(32, 32, 0, 32);
                                                header.setGravity(Gravity.CENTER_VERTICAL);
                                                header.setTextColor(Color.WHITE);
                                                header.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
                                                GridLayout.LayoutParams paramsHeader = new GridLayout.LayoutParams();
                                                paramsHeader.width = GridLayout.LayoutParams.WRAP_CONTENT;
                                                paramsHeader.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                                paramsHeader.setMargins(32, 0, 0, 0); //left, top, right, bottom
                                                header.setLayoutParams(paramsHeader);
                                                eventList.addView(header);

                                                int value = getIntent().getIntExtra("VALUE", 0);
                                                switch (value) {
                                                    case 0 -> {
                                                        // code block
                                                        GridLayout gridLayout = new GridLayout(eventList.getContext());
                                                        gridLayout.setColumnCount(2); // set the number of columns you want
                                                        for (int i = 0; i < events.size(); i++) {
                                                            Button button = new Button(eventList.getContext());
                                                            button.setId(i);
                                                            button.setText(sanitizeEventName(events.get(i).getEventName().toLowerCase()));
                                                            button.setTransformationMethod(null);

                                                            //Add button layout modification stuff to make it look nice here (target button)
                                                            button.setOnClickListener(new handleClick(events.get(i).getEventID()));
                                                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                                            params.setMargins(16, 16, 16, 32); //left, top, right, bottom
                                                            params.width = 440;
                                                            params.height = 350;
                                                            button.setLayoutParams(params);

                                                            //This will change the background of the CardView
                                                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                            int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(HomeScreen.this, R.color.Default_color));

                                                            // Create a GradientDrawable with the desired background color
                                                            GradientDrawable shape = new GradientDrawable();
                                                            shape.setShape(GradientDrawable.RECTANGLE);
                                                            shape.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
                                                            shape.setColor(savedColor);
                                                            cardView.setBackground(shape);

                                                            //This will change the background of the event blocks on the home screen
                                                            GradientDrawable shape2 = new GradientDrawable();
                                                            shape2.setShape(GradientDrawable.RECTANGLE);
                                                            shape2.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
                                                            shape2.setColor(Color.parseColor("#4B4B4B"));
                                                            button.setBackground(shape2);

                                                            button.setTextColor(Color.WHITE);
                                                            button.setTextSize(20);
                                                            button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                                                            gridLayout.addView(button);
                                                        }
                                                        GridLayout.LayoutParams paramsGridLayout = new GridLayout.LayoutParams();
                                                        paramsGridLayout.width = GridLayout.LayoutParams.MATCH_PARENT;
                                                        paramsGridLayout.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                                        paramsGridLayout.setMargins(40, 0, 32, 0); //left, top, right, bottom
                                                        gridLayout.setLayoutParams(paramsGridLayout);
                                                        eventList.addView(gridLayout);
                                                    }
                                                    case 1 -> {
                                                        // code block
                                                        GridLayout linearLayout = new GridLayout(eventList.getContext());
                                                        linearLayout.setColumnCount(1); // set the number of columns you want
                                                        for (int i = 0; i < events.size(); i++) {
                                                            Button button = new Button(eventList.getContext());
                                                            button.setId(i);
                                                            button.setText(sanitizeEventName(events.get(i).getEventName().toLowerCase()));
                                                            button.setTransformationMethod(null);
                                                            //Add button layout modification stuff to make it look nice here (target button)
                                                            button.setOnClickListener(new handleClick(events.get(i).getEventID()));
                                                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                                            params.setMargins(90, 16, 16, 32); //left, top, right, bottom
                                                            params.width = 750;
                                                            params.height = 650;
                                                            button.setLayoutParams(params);

                                                            //This will change the background of the CardView
                                                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                                            int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(HomeScreen.this, R.color.Default_color));

                                                            // Create a GradientDrawable with the desired background color
                                                            GradientDrawable shape = new GradientDrawable();
                                                            shape.setShape(GradientDrawable.RECTANGLE);
                                                            shape.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
                                                            shape.setColor(savedColor);
                                                            cardView.setBackground(shape);

                                                            //This will change the background of the event blocks on the home screen
                                                            GradientDrawable shape2 = new GradientDrawable();
                                                            shape2.setShape(GradientDrawable.RECTANGLE);
                                                            shape2.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
                                                            shape2.setColor(Color.parseColor("#4B4B4B"));
                                                            button.setBackground(shape2);

                                                            button.setTextColor(Color.WHITE);
                                                            button.setTextSize(27);
                                                            button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                                                            linearLayout.addView(button);
                                                        }
                                                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                                                        layoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
                                                        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                                                        layoutParams.setMargins(50, 0, 32, 0); //left, top, right, bottom
                                                        linearLayout.setLayoutParams(layoutParams);
                                                        eventList.addView(linearLayout);
                                                    }
                                                    default -> {
                                                    }
                                                }

                                            }

                                            class handleClick implements View.OnClickListener {
                                                final String eventID;

                                                handleClick(String eventID) {
                                                    this.eventID = eventID;
                                                }

                                                @Override
                                                public void onClick(View view) {
                                                    Log.d(TAG, eventID);
                                                    Intent intent = new Intent(getApplicationContext(), DisplayEventScreen.class);
                                                    intent.putExtra("eventID", eventID);
                                                    Log.d(TAG, "Event ID: " + intent.getStringExtra("eventID"));
                                                    startActivity(intent);
                                                }
                                            }

                                        }
                                        Log.d(TAG, events.toString());
                                        //update events list on GUI thread using anon class
                                        runOnUiThread(new updateEvents(events));
                                    }

                                    @Override
                                    public void onFailure(@NonNull Throwable thrown) {
                                    }
                                }, Giftly.service
                        );
                    }

                    @Override
                    public void onFailure(@NonNull Throwable thrown) {

                    }
                },
                Giftly.service
        );

        //Theme: Fetch the current color of the background
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(HomeScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_profile){
            startActivity(new Intent(HomeScreen.this, ProfileScreen.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public String sanitizeEventName(String eventName) {
        // Capitalize the first letter of the event name if it has a length
        if (eventName.length() > 1) {
            eventName = eventName.substring(0, 1).toUpperCase() + eventName.substring(1);
            eventName = eventName.toLowerCase();
            eventName = Character.toString(eventName.charAt(0)).toUpperCase() + eventName.substring(1);

            String[] temp = eventName.split(" ");
            StringBuilder results = new StringBuilder();
            for (String s : temp) {
                results.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
            }
            return results.toString();
        } else return getString(R.string.unnamed_event);
    }
}

