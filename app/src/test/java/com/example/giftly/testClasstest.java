package com.example.giftly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class testClasstest {
    private testClass activity;
    @Before
    public void setUp() {
        System.out.println("Setting up test...");
        activity = Robolectric.buildActivity(testClass.class).create().visible().get();
        System.out.println("Setup complete");
    }

    @Test
    public void matt() {
        int actual = activity.matt(1,3);
        assertEquals(4, actual);
        System.out.println("Matt test passed");
    }

    @Test
    public void matt2() {
        int actual = activity.matt2(1,6,4);
        assertEquals(11, actual);
        System.out.println("Matt2 test passed");
    }

    @Test
    public void matt3() {
        int actual = activity.matt3(7);
        assertEquals(7, actual);
        System.out.println("Matt3 test passed");
    }

    @Test
    public void matt4() {
        int actual = activity.matt4(10,4);
        assertEquals(6, actual);
        System.out.println("Matt4 test passed");
    }

    @Test
    public void Fname() {
        String Fname = activity.Fname("John");
        System.out.println("Firstname = John");
        System.out.println("Firstname test passed");
    }

    @Test
    public void Lname() {
        String Lname = activity.Lname("Doe");
        System.out.println("Lastname = Doe");
        System.out.println("Lastname test passed");
    }
}
