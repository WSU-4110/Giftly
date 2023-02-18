package com.example.giftly.handler;
import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

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
        eventName = event.get("eventName").toString();
        eventID = event.getId();
        ownerID = event.get("ownerID").toString();
        eventStartDate = ((Timestamp)event.get("eventStartDate")).toDate();
        participants = (ArrayList<String>)event.get("participants");
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
