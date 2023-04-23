package com.example.giftly;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TestUserInfo {
    private UserInformation activity;
    @Before
    public void setUp() {
        System.out.println("Setting up test...");
        activity = Robolectric.buildActivity(UserInformation.class).create().visible().get();
        System.out.println("Setup complete");
    }

    @Test
    public void email() {
        String actual = activity.email("test@gmail.com");
        assertEquals("test@gmail.com", actual);
        System.out.println("Email test passed");
    }

    @Test
    public void username() {
        String actual = activity.username("testUser");
        assertEquals("testUser", actual);
        System.out.println("Username test passed");
    }

    @Test
    public void phoneNumber() {
        String actual = activity.phoneNumber("313-123-4567");
        assertEquals("313-123-4567", actual);
        System.out.println("Phone Number test passed");
    }

    @Test
    public void address() {
        String actual = activity.address("123 Detroit");
        assertEquals("123 Detroit", actual);
        System.out.println("Address test passed");
    }

    @Test
    public void Fname() {
        String actual = activity.Fname("John");
        System.out.println("Firstname = John");
        assertEquals("John", actual);
        System.out.println("Firstname test passed");
    }

    @Test
    public void Lname() {
        String actual = activity.Lname("Doe");
        System.out.println("Lastname = Doe");
        assertEquals("Doe", actual);
        System.out.println("Lastname test passed");
    }
}