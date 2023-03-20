package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.giftly.handler.Event;
import com.example.giftly.handler.User;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DisplayEventScreen extends AppCompatActivity {

    public Button button_edit_event;
    private SharedPreferences sharedPreferences;

    ListView participantList;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_screen);

        // MAPVIEW TEST
        String lon = "-83.0717"; //get longitude
        String lat = "42.3502"; // get latitude
        String url = "https://api.mapbox.com/styles/v1/mapbox/outdoors-v11/static/pin-s+ff0000" + "(" + lon + "," + lat + ")/" + lon + "," + lat + ",9,0/344x127?access_token=pk.eyJ1IjoiaGczODA1IiwiYSI6ImNsZmR0bmdhYTA3dWkzcmxiOWdzY3M1MGgifQ.PtHaeSYNAvKWYzqqAS0v5A";

        ImageView mapView = (ImageView) findViewById(R.id.static_map);
        Picasso.get().load(url).into(mapView);


        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(DisplayEventScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Enable the back-button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent eventIntent = getIntent();
        String eventID = eventIntent.getStringExtra("eventID");

        if (eventIntent.getBooleanExtra("new", false)) {
            Log.d(TAG, "New Event Detected");
            Toast.makeText(DisplayEventScreen.this, "Event Successfully Created", Toast.LENGTH_SHORT).show();
        }


        Log.d(TAG, "EventID: " + eventID);

        participantList = findViewById(R.id.participant_list);
        TextView eventTitle = findViewById(R.id.Event_title);
        TextView eventDate = findViewById(R.id.event_date);

        Futures.addCallback(
                client.readEvent(eventID),
                new FutureCallback<Event>() {
                    class updateEventGui implements Runnable {
                        final Event event;

                        public updateEventGui(Event event) {
                            this.event = event;
                        }


                        // Displays event name and date; pulled from database
                        @Override
                        public void run() {
                            eventTitle.setText(event.getEventName());
                            eventDate.setText(event.getEventStartDate().toString());
                        }
                    }

                    @Override
                    public void onSuccess(Event event) {
                        Log.d(TAG, "Successfully pulled EventID" + event.getParticipants());
                        runOnUiThread(new updateEventGui(event));
                        ArrayList<String> participants = event.getParticipants();

                        participants.remove(client.getAuth().getUid());

                        Futures.addCallback(
                                client.readUser(event.getParticipants()),
                                new FutureCallback<ArrayList<User>>() {

                                    @Override
                                    public void onSuccess(ArrayList<User> users) {
                                        Log.d(TAG, "Successfully pulled Users" + users.toString());
                                        String[] participantNames = new String[users.size()];
                                        for (int i = 0; i < users.size(); i++) {
                                            participantNames[i] = users.get(i).getFullName();
                                        }
                                        runOnUiThread(new updateParticipants(participantNames, event.getParticipants(), eventID));
                                    }

                                    @Override
                                    public void onFailure(Throwable thrown) {
                                    }
                                }, Giftly.service);


                    }

                    @Override
                    public void onFailure(Throwable thrown) {
                    }
                }, Giftly.service);

        // This section is commented out until we add the edit event page?
//        button_edit_event = (Button) findViewById(R.id.edit_event);
//        button_edit_event.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DisplayEventScreen.this, edit_event_screen.class);
//                startActivity(intent);
//            }
//        });
        // The list of participants is displayed on the screen



    }
    class updateParticipants implements Runnable {
        String[] participantNames;
        String[] participantIDs;
        String eventID;

        updateParticipants(String[] names, ArrayList<String> p, String eventID) {
            participantNames = names;
            participantIDs = p.toArray(new String[p.size()]);
            this.eventID = eventID;

        }

        @Override
        public void run() {
            ArrayAdapter<String> arr = new ArrayAdapter<String>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, participantNames);
            participantList.setAdapter(arr);
            participantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(DisplayEventScreen.this, GiftSignup.class);
                    i.putExtra("eventID", eventID);
                    i.putExtra("userID", participantIDs[position]);
                    startActivity(i);
                }
            });
        }
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

