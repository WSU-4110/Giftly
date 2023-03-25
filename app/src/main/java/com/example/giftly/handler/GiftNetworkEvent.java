package com.example.giftly.handler;
import static android.content.ContentValues.TAG;

import android.util.Log;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GiftNetworkEvent extends AbstractEvent {
    String eventID;
    String eventOwner;

    //Event Info
    String eventName;
    Date eventStartDate;
    ArrayList<String> participants;



    //Local Constructor with ID's confirmed
    public GiftNetworkEvent(String eventID, String ownerID, String eventName, Date eventStartDate) {
        this.eventID = eventID;
        this.eventOwner = ownerID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }

    //Pulls data from document snapshot
    public GiftNetworkEvent(DocumentSnapshot event) {

        try {
            eventName = event.contains("eventName") ? Objects.requireNonNull(event.get("eventName")).toString() : "No Event Name";
            eventID = event.getId();
            eventStartDate = event.contains("eventStartDate") ? ((Timestamp) Objects.requireNonNull(event.get("eventStartDate"))).toDate() : new Date();
            participants = event.contains("participants") ? (ArrayList<String>) event.get("participants") : new ArrayList<>(1);
            eventOwner = event.contains("ownerID") ? Objects.requireNonNull(event.get("ownerID")).toString() : participants.get(0);
        }
        catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public Map<String, Object> convertToDocument() {
        Map<String, Object> eventDoc = new HashMap<>();
        //get basic event data
        eventDoc.put("eventName", eventName);
        eventDoc.put("eventStartDate", eventStartDate);
        eventDoc.put("eventOwner", eventOwner);  //sets the owner to the creator
        eventDoc.put("participants", participants);  //adds an array list with just the event creator in it
        return eventDoc;
    }

    //Reg accessors
    public String getEventID() {
        return eventID;
    }
    public String getEventOwner() {
        return eventOwner;
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
