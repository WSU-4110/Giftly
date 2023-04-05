package com.example.giftly;

import static androidx.fragment.app.FragmentManager.TAG;
import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.example.giftly.handler.EventBuilder;
import com.example.giftly.handler.GiftNetworkEvent;
import com.example.giftly.handler.IEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class EventBuilderTests {

    IEvent test;
    HashMap<String, Object> benchMap;

    @Before
    public void setUp() {
        System.out.println("Setting up test...");
        benchMap = new HashMap<>(4);
        benchMap.put("eventStartDate", new Date());
        benchMap.put("eventName", "Birthday");
        benchMap.put("eventType", 0);
        benchMap.put("eventID", "TestID");
        benchMap.put("ownerID", "TestOwner");
        test = EventBuilder.createEvent(benchMap);

        benchMap.put("participants", new ArrayList<String>(1));
        benchMap.remove("eventID");

        System.out.println("Setup complete with dummy date: " + test.toString());

    }

    //Test that EventBuilder creates the correct class
    @Test
    public void EventFactoryTest() {
        System.out.println("EventBuilder Built class: " + test.getClass());
        assertEquals(GiftNetworkEvent.class, test.getClass());
        System.out.println("EventBuilderType Passed");
    }

    //Check participants matches expected when loading from document
    @Test
    public void participantsTest() {
        System.out.println("Retrieve Participants test:");
        assertEquals(new ArrayList<String>(1), test.getParticipants());
        System.out.println("Test passed");
    }

    //Test ID is returned
    @Test
    public void eventIDTest() {
        assertEquals("TestID", test.getEventID());
    }

    //Test Conversion returns the exact same object
    @Test
    public void convertToDocument() {
        Map<String, Object> testDocument = test.convertToDocument();
        assertEquals(benchMap.get("eventName"), testDocument.get("eventName"));
        assertEquals(benchMap.get("eventType"), testDocument.get("eventType"));
        assertEquals(benchMap.get("ownerID"), testDocument.get("ownerID"));
        assertEquals(benchMap.get("eventStartDate"), testDocument.get("eventStartDate"));
        assertEquals(benchMap.get("participants"), testDocument.get("participants"));

        System.out.println("Test passed");
    }


    //Test Name modification
    @Test
    public void modifyName() {
        test.setEventName("");
        assertEquals(test.getEventName(), "No Name Found");

        System.out.println("Over Limit Test...");
        test.setEventName("LOKINHFYEGTHELROWKERasfafaewfawefawefawef");
        assertEquals(test.getEventName(), "LOKINHFYEGTHELROWKER");

        System.out.println("Normal Operation...");
        test.setEventName("Birthday");
        assertEquals(test.getEventName(), "Birthday");

        System.out.println("Null test...");
        test.setEventName(null);
        System.out.println("Buffer Test");
        test.setEventName("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        System.out.println("Test passed");
    }

    @Test
    public void addParticipant() {
        ArrayList<String> benchmark = new ArrayList<String>(1);
        benchmark.add("Test ID");

        System.out.println("Initial addition...");
        test.addParticipant("Test ID");
        assertEquals(test.getParticipants(), benchmark);

        System.out.println("Duplicate test...");
        test.addParticipant("Test ID");
        assertEquals(test.getParticipants(), benchmark);

        System.out.println("Null test...");
        test.addParticipant(null);

        System.out.println("Test passed");
    }


}
