package com.example.giftly.handler;
import static android.content.ContentValues.TAG;

import android.util.Log;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Event {
    String eventID;
    String ownerID;

    //Event Info
    String eventName;
    Date eventStartDate;
    ArrayList<String> participants;



    //Local Constructor with ID's confirmed
    public Event(String eventID, String ownerID, String eventName, Date eventStartDate) {
        this.eventID = eventID;
        this.ownerID = ownerID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }

    //Pulls data from document snapshot
    public Event(DocumentSnapshot event) {

        try {
            eventName = event.contains("eventName") ? Objects.requireNonNull(event.get("eventName")).toString() : "No Event Name";
            eventID = event.getId();
            eventStartDate = event.contains("eventStartDate") ? ((Timestamp) Objects.requireNonNull(event.get("eventStartDate"))).toDate() : new Date();
            participants = event.contains("participants") ? (ArrayList<String>) event.get("participants") : new ArrayList<>(1);
            ownerID = event.contains("ownerID") ? Objects.requireNonNull(event.get("ownerID")).toString() : participants.get(0);
        }
        catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }
    }

    //Reg accessors
    public String getEventID() {
        return eventID;
    }
    public String getOwnerID() {
        return ownerID;
    }
    public String getEventName() {
        return eventName;
    }
    public Date getEventStartDate() {
        return eventStartDate;
    }
    public ArrayList<String> getParticipants() {
        return participants;
    }
}
