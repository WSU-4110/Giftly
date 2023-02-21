package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.giftly.handler.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {
    public Button settingsBtn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        //find Users name and display it to test readUser data
        TextView display = findViewById(R.id.TestDisplay);
        settingsBtn = (Button) findViewById(R.id.SettingsBtn);
        settingsBtn.setBackgroundColor(Color.TRANSPARENT);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, SettingsScreen.class);
                startActivity(intent);
            }
        });

        //This call will display the user's name in the greeting message
        Futures.addCallback(
                Giftly.client.readUser(client.getAuth().getUid()),
                new FutureCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        display.setText(result.getFullName());
                        //This call will display the user's name in the greeting message
                        Futures.addCallback(
                                Giftly.client.readEvent(result.getEvents()),
                                new FutureCallback<ArrayList<Event>>() {
                                    @Override
                                    public void onSuccess(ArrayList<Event> events) {
                                        //anon class for updating GUI thread
                                        class updateEvents implements Runnable {
                                            ArrayList<Event> events;
                                            updateEvents(ArrayList<Event> e) { events = e;}
                                            @Override
                                            public void run() {
                                                LinearLayout eventList = findViewById(R.id.events);
                                                Log.d(TAG, "Adding Events to List:");

                                                //Loops through arraylist of events and makes a new button for each event.
                                                for (int i = 0; i < events.size(); i++) {
                                                    Button button = new Button(eventList.getContext());
                                                    button.setId(i);
                                                    Log.d(TAG, String.format("Added event %d name %s", i, events.get(i)));
                                                    button.setText(events.get(i).getEventName());
                                                    //Add button layout modification stuff to make it look nice here (target button)
                                                    button.setOnClickListener(new handleClick(events.get(i).getEventID()));
                                                    eventList.addView(button);
                                                    //Assuming your button is in a LinearLayout as stated
                                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
                                                    params.setMargins(0, 0, 0, 25); //left, top, right, bottom
                                                    button.setLayoutParams(params);
                                                    button.setBackgroundColor(Color.parseColor("#4B4B4B"));
                                                    button.setTextColor(Color.WHITE);
                                                    button.setTextSize(16);
                                                    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD));
                                                }
                                            }

                                            class handleClick implements View.OnClickListener {
                                                String eventID;
                                                handleClick(String eventID) {
                                                    this.eventID = eventID;
                                                }
                                                @Override
                                                public void onClick(View view) {
                                                    Log.d(TAG, eventID);
                                                    Intent intent = new Intent(getApplicationContext(),DisplayEventScreen.class);
                                                    intent.putExtra("eventID", eventID.toString());
                                                    Log.d(TAG, "Event ID: " + intent.getStringExtra("eventID"));
                                                    startActivity(intent);
                                                }
                                            }

                                        };
                                        Log.d(TAG, events.toString());
                                        //update events list on GUI thread using anon class
                                        runOnUiThread(new updateEvents(events));
                                    }
                                    @Override
                                    public void onFailure(Throwable thrown) {
                                    }
                                }, Giftly.service
                        );
                    }

                    @Override
                    public void onFailure(Throwable thrown) {

                    }
                },
                Giftly.service
        );

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
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



}

