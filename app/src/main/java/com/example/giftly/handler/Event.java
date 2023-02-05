package com.example.giftly.handler;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Date;

public class Event {
    String eventID;
    String ownerID;

    //Event Info
    String eventName;
    Date eventStartDate;
    String[] participants;



    //Local Constructor with ID's confirmed
    public Event(String eventID, String ownerID, String eventName, Date eventStartDate) {
        this.eventID = eventID;
        this.ownerID = ownerID;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }

    //Pulls data from document snapshot
    public Event(DocumentSnapshot event) {
        eventID = event.getId();
        ownerID = event.get("OwnerID").toString();
        eventStartDate = ((Timestamp)event.get("eventStartDate")).toDate();
        participants = (String[])event.get("participants");
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
    public String[] getParticipants() {
        return participants;
    }
}
