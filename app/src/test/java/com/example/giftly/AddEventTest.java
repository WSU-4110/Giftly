package com.example.giftly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

import java.util.Calendar;

@RunWith(RobolectricTestRunner.class)
public class AddEventTest {
    private AddEventScreen testing;

    @Before
    public void setUp() {
        System.out.println("Setting up test...");
        testing = Robolectric.buildActivity(AddEventScreen.class).create().get();
        System.out.println("Setup complete");
    }


    // Testing Button to Cancel Adding an Event
    @Test
    public void onCancelAddingEvent() {

        System.out.println("Testing Adding Event Button...");

        Button button_cancel_adding = (Button) testing.findViewById( R.id.button_cancel );
        assertNotNull(button_cancel_adding);
        button_cancel_adding.performClick();
        Intent intent = Shadows.shadowOf(testing).peekNextStartedActivity();
        assertEquals(HomeScreen.class.getCanonicalName(), intent.getComponent().getClassName());

    }

    // Testing Button to Adding an Event
    @Test
    public void onAddingEvent() {

        System.out.println("Testing Cancel Adding Event Button...");

        Button button_create_event = (Button) testing.findViewById( R.id.button_cancel );
        assertNotNull(button_create_event);
        button_create_event.performClick();
        Intent intent = Shadows.shadowOf(testing).peekNextStartedActivity();
        assertEquals(HomeScreen.class.getCanonicalName(), intent.getComponent().getClassName());

    }


    // Testing that setting calendar year input works appropriately
    @Test
    public void testCalenarYearInput() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);

        int year = 2023;

        assertEquals(year, calendar.get(Calendar.YEAR));

        System.out.println("Testing Updating Date Text...");

    }

    // Testing that setting calendar month input works appropriately
    @Test
    public void testCalenarMonthInput() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 8);

        int month = 8;

        assertEquals(month, calendar.get(Calendar.MONTH));

        System.out.println("Testing Updating Date Text...");

    }

    // Testing that setting calendar day input works appropriately
    @Test
    public void testCalenarDayInput() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 24);

        int day = 24;

        assertEquals(day, calendar.get(Calendar.DAY_OF_MONTH));

        System.out.println("Testing Updating Date Text...");

    }

    // Testing that textview will appropriately display the calendar inputs
    @Test
    public void testCalendarUpdate() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.DAY_OF_MONTH,05);

        int year = 2023;
        int month = 8;
        int day = 05;

        String result = String.format("%d/%d/%d", day,month,year);

        TextView textbox_eventDate = testing.findViewById(R.id.enter_date);
        textbox_eventDate.setText(result);

        assertEquals(textbox_eventDate.getText().toString(), result);
        System.out.println("Testing Updating Date Text...");

    }





}

