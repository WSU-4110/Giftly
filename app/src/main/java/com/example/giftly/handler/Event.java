package com.example.giftly.handler;

import java.util.Date;

public class Event {
    String eventID;
    String ownerID;

    //Event Info
    String eventName;
    Date eventStartDate;
    User[] Participants;



    public Event(String eventID, String ownerID, String eventName, Date eventStartDate) {
        this.eventID = eventID;
        this.ownerID = ownerID;
        eventName = eventName;
        this.eventStartDate = eventStartDate;
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
    public User[] getParticipants() {
        return Participants;
    }
}
