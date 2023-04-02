package com.example.giftly.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public abstract class Event implements IEvent{
    String eventID;
    String ownerID;

    //Event Info
    String eventName;
    Date eventStartDate;
    ArrayList<String> participants;

    //Reg accessors
    public String getEventID() {
        return eventID;
    }
    public String getEventOwner() {
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
    public abstract ArrayList<String> getRecipients();
    public abstract Map<String, Object> convertToDocument();
    public abstract ArrayList<String> addParticipant(String userID);

    public void setEventName(String name) {
        if (name.length() > 0) {
            eventName = name.substring(0, Math.min(name.length(), 20));
        }
        else {
            eventName = "No Name Found";
        }
    }
}
