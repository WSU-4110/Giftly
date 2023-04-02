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
        benchMap = new HashMap<>(4);
        benchMap.put("eventStartDate", new Date());
        benchMap.put("eventName", "Birthday");
        benchMap.put("eventType", 0);
        benchMap.put("eventID", "TestID");
        benchMap.put("ownerID", "TestOwner");
        test = EventBuilder.createEvent(benchMap);

        benchMap.put("participants", new ArrayList<String>(1));
        benchMap.remove("eventID");
    }

    //Test that EventBuilder creates the correct class
    @Test
    public void EventFactoryTest() {
        Log.d(TAG, test.getClass().toString());
        assertEquals(GiftNetworkEvent.class, test.getClass());
    }

    //Check participants matches expected when loading from document
    @Test
    public void participantsTest() {
        assertEquals(new ArrayList<String>(1), test.getParticipants());
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
    }


    //Test Name modification
    @Test
    public void modifyName() {
        test.setEventName("");
        assertEquals(test.getEventName(), "No Name Found");

        test.setEventName("LOKINHFYEGTHELROWKERasfafaewfawefawefawef");
        assertEquals(test.getEventName(), "LOKINHFYEGTHELROWKER");

        test.setEventName("Birthday");
        assertEquals(test.getEventName(), "Birthday");

        test.setEventName(null);
        test.setEventName("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    @Test
    public void addParticipant() {
        ArrayList<String> benchmark = new ArrayList<String>(1);
        benchmark.add("Test ID");

        test.addParticipant("Test ID");
        assertEquals(test.getParticipants(), benchmark);

        test.addParticipant("Test ID");
        assertEquals(test.getParticipants(), benchmark);

        test.addParticipant(null);
    }


}
