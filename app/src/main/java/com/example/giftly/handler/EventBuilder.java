package com.example.giftly.handler;

public abstract class EventBuilder {
    public static String[] eventTypes = {};
    public abstract AbstractEvent createEvent();

}
