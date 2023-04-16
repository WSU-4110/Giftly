package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.giftly.handler.IEvent;
import com.example.giftly.handler.User;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mapbox.maps.AsyncOperationResultCallback;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.common.AsyncOperationTask;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DisplayEventScreen extends AppCompatActivity {

    public Button button_edit_event;
    public Button leaveEventBtn;
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


        leaveEventBtn = (Button) findViewById(R.id.leave_event);

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
        TextView eventTitleDisplay = findViewById(R.id.Event_title);
        TextView eventDateDisplay = findViewById(R.id.event_date);

        Futures.addCallback(
                client.readEvent(eventID),
                new FutureCallback<IEvent>() {
                    class updateEventGui implements Runnable {
                        final IEvent event;

                        public updateEventGui(IEvent event) {
                            this.event = event;
                        }


                        // Displays event name and date; pulled from database
                        @Override
                        public void run() {
                            String eventName = event.getEventName();
                            Date eventDate = event.getEventStartDate();

                            eventTitleDisplay.setText(eventName  == null ? "Unnamed Event" : eventName);
                            eventDateDisplay.setText(eventDate == null ? "No Date Set" : eventDate.toString());
                        }
                    }

                    @Override
                    public void onSuccess(IEvent event) {
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

        //This section is commented out until we add the edit event page?
        button_edit_event = (Button) findViewById(R.id.edit_event);
        button_edit_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayEventScreen.this, AddEventScreen.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            }
        });
        // The list of participants is displayed on the screen


        leaveEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            //Pop Up for leaving an event
            public void onClick(View v) {

                Futures.addCallback(
                        client.leaveEvent(eventID),
                        new FutureCallback<String>() {

                            @Override
                            public void onSuccess(String result) {
                                Log.d(TAG, "Successfully left event");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DisplayEventScreen.this, result, Toast.LENGTH_SHORT).show();
                                        //Pop up to leave event
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayEventScreen.this);
                                        builder.setMessage("You've left this event")
                                                .setTitle("Leave Event Request")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(DisplayEventScreen.this, HomeScreen.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Throwable thrown) {

                                String message = (thrown.getMessage().isEmpty() ? "There was a problem leaving the event" : thrown.getMessage());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DisplayEventScreen.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.d(TAG, thrown.toString());
                            }
                        }, Giftly.service);




            }


        });
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

