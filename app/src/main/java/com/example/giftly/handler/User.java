package com.example.giftly.handler;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.net.URL;
import java.util.Map;

public class User {
    String userID;
    String fullName;
    String[] interests;
    String[] events;
    URL profilePicture; //TODO


    //Test Constructor
    public User() {
        fullName = "John Doe";
        interests = new String[]{"Cheeses", "Wines", "BBQ Accessories", "PS5 Games", "Pet Rocks"};
    }


    public User(String fullName, String[] interests, String[] events) {
        this.fullName = fullName;
        this.interests = interests;
        this.events = events;
    }

    //pulls data from a document snapshot
    public User(DocumentSnapshot user) {
        try {
            userID = user.getId();
            fullName = user.get("Name").toString();
            interests = (String[]) (user.get("Interests"));
            events = (String[]) (user.get("Events"));
        }
        catch (NullPointerException e) {
            Log.d(TAG, "Imported incomplete document, remaining values nulled");
        }
        finally {
            Log.d(TAG, "Objecyt");
        }
    }

    //getUser Info
    public String getFullName() {
        return fullName;
    }
    public String[] getInterests() {
        return interests;
    }
    public String[] getEvents() {
        return events;
    }
}
