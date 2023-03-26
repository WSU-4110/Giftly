package com.example.giftly.handler;

import java.util.ArrayList;
import java.util.Date;

public interface IEvent {
    //Reg accessors
    public String getEventID();
    public String getEventOwner();
    public String getEventName();
    public Date getEventStartDate();
    public ArrayList<String> getParticipants();
}
