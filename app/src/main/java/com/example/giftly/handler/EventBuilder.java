package com.example.giftly.handler;

import static android.content.ContentValues.TAG;
import android.util.Log;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Map;

public class EventBuilder {
    //Creates an event without an eventID (unless one is provided from the over-loaded method) from local creation
    public static IEvent createEvent(Map<String, Object> eventMap) {
        try {
            Object eventTypeObject = eventMap.getOrDefault("eventType", 0);
            int eventType;
            try {
                eventType = (int)eventTypeObject;
            }
            catch (ClassCastException e) {
                Log.d(TAG, "Test Event Loaded");
                eventType = ((Long)eventTypeObject).intValue();
            }

            switch (eventType) {
                case 0:
                    return new GiftNetworkEvent(eventMap);
                default:
                    Log.d(TAG, "Invalid EventType ID");
                    return null;
            }

        } catch (NullPointerException e) {
            return null;
        }
    }

    //Creates an event with an eventID from firebaseClientStorage
    public static IEvent createEvent(DocumentSnapshot Document) {
        Map<String, Object> eventMap = Document.getData();
        eventMap.put("eventID", Document.getId());
        return createEvent(eventMap);
    }
}
