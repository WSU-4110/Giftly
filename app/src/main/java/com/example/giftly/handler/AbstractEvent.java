package com.example.giftly.handler;
import static android.content.ContentValues.TAG;

import android.util.Log;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractEvent {
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

    public abstract Map<String, Object> convertToDocument();

}
