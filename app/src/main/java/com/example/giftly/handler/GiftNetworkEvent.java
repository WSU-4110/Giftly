package com.example.giftly.handler;
import static android.content.ContentValues.TAG;

import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GiftNetworkEvent extends Event {
    //Local Constructor with ID's confirmed

    GiftNetworkEvent(String eventID, String ownerID, String eventName, Date eventStartDate) {
        this.eventID = eventID;
        this.ownerID = ownerID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }

    //Pulls data from document snapshot
    GiftNetworkEvent(Map<String, Object> event) {
        try {
            eventName = (Objects.requireNonNull(event.getOrDefault("eventName", "Unnamed Event"))).toString();
            eventID = Objects.requireNonNull(event.getOrDefault("eventID", "No ID")).toString();
            eventStartDate = ((Date) (event.getOrDefault("eventStartDate", null)));
            participants = (ArrayList<String>) event.getOrDefault("participants", new ArrayList<>(1));
            ownerID = (Objects.requireNonNull(event.getOrDefault("ownerID", "No Owner Found"))).toString();
        } catch (NullPointerException e) {
            Log.d(TAG, "Invalid Document Data: " + e.toString());
        }
    }

    @Override
    public Map<String, Object> convertToDocument() {
        Map<String, Object> eventDocument = new HashMap<>();
        //get basic event data
        eventDocument.put("eventName", eventName);
        eventDocument.put("eventStartDate", eventStartDate);
        eventDocument.put("ownerID", ownerID);  //sets the owner to the creator
        eventDocument.put("participants", participants);  //adds an array list with just the event creator in it
        eventDocument.put("eventType", 0);
        return eventDocument;
    }

    @Override
    public ArrayList<String> addParticipant(String userID) {
        //Check if event already has the user, if not add them and update the doc
        if (participants == null)
            participants = new ArrayList<String>(1);
        Log.d(TAG, "Checking Event");
        if (!participants.contains(userID)) {
            participants.add(userID);
        }
        return participants;
    }

    //Reg accessors
    @Override
    public ArrayList<String> getRecipients() {
        return participants;
    }

    @Override
    public int getEventType() {
        return 0;
    }
}