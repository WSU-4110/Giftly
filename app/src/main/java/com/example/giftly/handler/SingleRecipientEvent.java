package com.example.giftly.handler;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingleRecipientEvent extends Event {
    //Local Constructor with ID's confirmed
    ArrayList<String> subject;


    SingleRecipientEvent(String eventID, String ownerID, String eventName, Date eventStartDate, String subject) {
        this.eventID = eventID;
        this.ownerID = ownerID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        ArrayList<String> recipients = new ArrayList<>(1);
        recipients.add(subject);
        this.subject = recipients;
    }

    //Pulls data from document snapshot
    SingleRecipientEvent(Map<String, Object> event) {

        try {
            eventName = (Objects.requireNonNull(event.getOrDefault("eventName", "Unnamed Event"))).toString();
            eventID = event.get("eventID").toString();
            eventStartDate = ((Date)(event.getOrDefault("eventStartDate", null)));
            participants = (ArrayList<String>) event.getOrDefault("participants", new ArrayList<>(1));
            ownerID = (Objects.requireNonNull(event.getOrDefault("ownerID", "No Owner Found"))).toString();
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
        eventDocument.put("ownerID", ownerID);  //sets the owner to the creator
        eventDocument.put("participants", participants);  //adds an array list with just the event creator in it
        eventDocument.put("eventType", 0);
        return eventDocument;
    }

    //Reg accessors

    @Override
    public ArrayList<String> getRecipients() {
        return (subject);
    }

    //TODO IMPLEMENT METHOD
    @Override
    public ArrayList<String> addParticipant(String userID) {
        return null;
    }
}
