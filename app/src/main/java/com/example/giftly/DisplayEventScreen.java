package com.example.giftly;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.giftly.handler.Event;
import com.example.giftly.handler.User;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class DisplayEventScreen extends AppCompatActivity {

    public Button button_edit_event;

    // Temporary List of Participants; array to be filled in by database info
    ListView participantList;



    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_screen);

        Intent eventIntent = getIntent();
        String eventID = eventIntent.getStringExtra("eventID");


        Log.d(TAG, "EventID: " + eventID);

        participantList = findViewById(R.id.participant_list);



        Futures.addCallback(
                Giftly.client.readEvent(eventID),
                new FutureCallback<Event>() {
                    @Override
                    public void onSuccess(Event event) {
                        Log.d(TAG, "Successfully pulled EventID" + event.getParticipants());

                        runOnUiThread(new updateEventGui());

                        Futures.addCallback(
                                Giftly.client.readUser(event.getParticipants()),
                                new FutureCallback<ArrayList<User>>() {
                                    @Override
                                    public void onSuccess(ArrayList<User> users) {
                                        Log.d(TAG, "Successfully pulled Users" + users.toString());
                                        String[] participantNames = new String[users.size()];

                                        for(int i = 0; i < users.size(); i++) participantNames[i] = users.get(i).getFullName();


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
            ArrayAdapter<String> arr = new ArrayAdapter<String>(getApplicationContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, participantNames);
            participantList.setAdapter(arr);
            participantList.setOnItemClickListener(new AdapterView.OnItemClickListener()

            {
                @Override
                public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                        Intent i = new Intent(DisplayEventScreen.this, GiftSignup.class);
                        i.putExtra("eventID", eventID);
                        i.putExtra("userID", participantNames[position]);
                        startActivity(i);
                }
            });
        }
    }

    class updateEventGui implements Runnable {


        @Override
        public void run() {

        }
    }
}

