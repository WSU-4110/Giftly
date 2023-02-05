package com.example.giftly.handler;

import java.net.URL;
import java.util.Map;

public class User {
    String fullName;
    String[] interests;
    Event[] events;
    URL profilePicture; //in prog


    //Test Constructor
    public User() {
        fullName = "John Doe";
        interests = new String[]{"Cheeses", "Wines", "BBQ Accessories", "PS5 Games", "Pet Rocks"};
    }


    public User(String fullName, String[] interests, Event[] events) {
        this.fullName = fullName;
        this.interests = interests;
        this.events = events;
    }

    public User(Map<String, Object> object) {
        fullName = fullName;
        interests = interests;
        events = events;
    }

    //getUser Info
    public String getFullName() {
        return fullName;
    }
    public String[] getInterests() {
        return interests;
    }
    public Event[] getEvents() {
        return events;
    }
}
