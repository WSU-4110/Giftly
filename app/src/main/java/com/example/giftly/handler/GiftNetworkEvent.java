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

class GiftNetworkEvent extends Event {
    //Local Constructor with ID's confirmed

    public GiftNetworkEvent(String eventID, String ownerID, String eventName, Date eventStartDate) {
        this.eventID = eventID;
        this.ownerID = ownerID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }

    //Pulls data from document snapshot
    public GiftNetworkEvent(Map<String, Object> event) {

        try {
            eventName = (Objects.requireNonNull(event.getOrDefault("eventName", "Unnamed Event"))).toString();
            eventID = event.get("eventID").toString();
            eventStartDate = ((Timestamp)(event.getOrDefault("eventStartDate", null))).toDate();
            participants = (ArrayList<String>) event.getOrDefault("participants", new ArrayList<>(1));
            ownerID = (event.getOrDefault("ownerID", "No Owner Found")).toString();
        }
        catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public Map<String, Object> convertToDocument() {
        Map<String, Object> eventDocument = new HashMap<>();
        //get basic event data
        eventDocument.put("eventName", eventName);
        eventDocument.put("eventStartDate", eventStartDate);
        eventDocument.put("eventOwner", ownerID);  //sets the owner to the creator
        eventDocument.put("participants", participants);  //adds an array list with just the event creator in it
        return eventDocument;
    }

    //Reg accessors

    @Override
    public ArrayList<String> getRecipients() {
        return participants;
    }
}
