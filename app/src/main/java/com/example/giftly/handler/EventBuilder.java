package com.example.giftly.handler;
import static android.content.ContentValues.TAG;
import android.util.Log;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Map;
import java.util.Objects;

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
                eventType = ((Long) eventTypeObject).intValue();
            }
            switch (eventType) {
                case 0:
                    Log.d(TAG, "Gift Event ");
                    return new GiftNetworkEvent(eventMap);
                case 1:
                    Log.d(TAG, "Single Recipient Event ");
                    return new SingleRecipientEvent(eventMap);
                default:
                    Log.d(TAG, "Invalid EventType ID");
                    return null;
            }

        } catch (NullPointerException e) {
            Log.d(TAG, "Invalid Document Data.");
            return null;
        }
    }

    //Creates an event with an eventID from firebaseClientStorage
    public static IEvent createEvent(DocumentSnapshot Document) {
        Map<String, Object> eventMap = Document.getData();
        Objects.requireNonNull(eventMap).put("eventID", Document.getId());
        if (eventMap.containsKey("eventStartDate"))
            eventMap.replace("eventStartDate", ((Timestamp) Objects.requireNonNull(eventMap.get("eventStartDate"))).toDate());
        return createEvent(eventMap);
    }
}
