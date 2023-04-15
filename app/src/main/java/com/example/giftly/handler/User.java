package com.example.giftly.handler;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class User {
    String userID;
    String fullName;
    ArrayList<String> interests;
    ArrayList<String> events;
    URL profilePicture; //TODO

    //Test Constructor
    public User() {
        fullName = "John Doe";
        interests = new ArrayList<>(0);
    }

    public User(String fullName, ArrayList<String> interests, ArrayList<String> events) {
        this.fullName = fullName;
        this.interests = interests;
        this.events = events;
    }

    //pulls data from a document snapshot
    public User(DocumentSnapshot user) {
        try {
            userID = user.getId();
            fullName = user.get("Name").toString();
            interests = (ArrayList<String>) (user.get("Interests"));
            events = (ArrayList<String>)(user.get("Events"));
        }
        catch (NullPointerException e) {
            Log.d(TAG, "Imported incomplete document, remaining values nulled");
        }
        finally {
            Log.d(TAG, "Object");
        }
    }

    //getUser Info
    public String getFullName() {
        return fullName;
    }
    public ArrayList<String> getInterests() {
        return interests;
    }
    public ArrayList<String> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return (String.format("Name: %s, Interests: %s", getFullName(), getInterests()));
    }
}
