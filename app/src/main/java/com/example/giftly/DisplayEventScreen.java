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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
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
                        runOnUiThread(new updateParticipants(event.getParticipants()));
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
        String[] participants;

        updateParticipants(ArrayList<String> p) {participants = p.toArray(new String[p.size()]);}

        @Override
        public void run() {
            ArrayAdapter<String> arr =new ArrayAdapter<String>(getApplicationContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, participants);
            participantList.setAdapter(arr);
            participantList.setOnItemClickListener(new AdapterView.OnItemClickListener()

            {
                @Override
                public void onItemClick (AdapterView < ? > parent, View view,int position, long id){

                    if (position == 0) {
                        Intent i = new Intent(DisplayEventScreen.this, GiftSignup.class);

                        startActivity(i);
                    }
                }
            });
        }
    }
}

