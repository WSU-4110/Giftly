package com.example.giftly.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public abstract class Event implements IEvent{
    String eventID;
    String ownerID;

    //Event Info
    String eventName;
    String eventLocation;
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

    public String getLocation(){return eventLocation; }
    public Date getEventStartDate() {
        return eventStartDate;
    }
    public ArrayList<String> getParticipants() {
        return participants;
    }
    public ArrayList<String> removeParticipant (String userID) {

        if (participants != null)
            participants.remove(userID);

        return participants;
    }
    public abstract ArrayList<String> getRecipients();
    public abstract Map<String, Object> convertToDocument();
    public abstract ArrayList<String> addParticipant(String userID);

    public void setEventName(String name) {
        if (name != null && !name.isEmpty()) {
            eventName = name.substring(0, Math.min(name.length(), 20));
        }
        else {
            eventName = "No Name Found";
        }
    }

    public void setEventLocation(String location) {
        if (location != null && !location.isEmpty()) {
            eventLocation = location.substring(0, Math.min(location.length(), 40));
        }
        else {
            eventLocation = "No Location Found";
        }
    }

}
