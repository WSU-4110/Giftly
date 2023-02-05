package com.example.giftly.handler;

import java.net.URL;

public class User {
    String fullName;
    String[] interests;
    Event[] events;
    URL profilePicture; //in prog

    public User(String fullName, String[] interests, Event[] events) {
        this.fullName = fullName;
        this.interests = interests;
        this.events = events;
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
