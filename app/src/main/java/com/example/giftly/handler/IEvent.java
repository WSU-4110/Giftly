package com.example.giftly.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface IEvent {
    //Reg accessors
    public String getEventID();
    public String getEventOwner();
    public String getEventName();
    public Date getEventStartDate();
    public ArrayList<String> getParticipants();
    //Convert to Doc
    public Map<String, Object> convertToDocument();
    //Reg Modifiers
    public void setEventName(String name);
    public ArrayList<String> addParticipant(String userID);

}
