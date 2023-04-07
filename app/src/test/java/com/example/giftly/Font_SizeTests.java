package com.example.giftly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class Font_SizeTests {
    private FontSizeScreen activity;

    @Before
    public void setUp() {
        System.out.println("Setting up test...");
        activity = Robolectric.buildActivity(FontSizeScreen.class).create().visible().get();
        System.out.println("Setup complete");
    }

    @Test
    public void testOnCreate() {
        System.out.println("Testing testOnCreate()");
        FontSizeScreen activity = Robolectric.setupActivity(FontSizeScreen.class);
        assertNotNull(activity);
        System.out.println("Test passed");
    }

    @Test
    public void testFontSizeSeekBar() {
        System.out.println("Testing testFontSizeSeekBar()");
        FontSizeScreen activity = Robolectric.setupActivity(FontSizeScreen.class);
        SeekBar fontSizeSeekBar = activity.findViewById(R.id.fontSizeButton);

        fontSizeSeekBar.setProgress(10);

        SharedPreferences preferences = activity.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        float savedSize = preferences.getFloat("font_size", 16);
        assertEquals(14f, savedSize, 0.01f);
        System.out.println("Test passed");
    }

    @Test
    public void testIterateViews() {
        System.out.println("Testing testIterateViews");
        TextView textView = new TextView(activity.getApplicationContext());
        EditText editText = new EditText(activity.getApplicationContext());
        Button button = new Button(activity.getApplicationContext());
        ViewGroup rootView = new ViewGroup(activity.getApplicationContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {}
        };
        rootView.addView(textView);
        rootView.addView(editText);
        rootView.addView(button);

        float textSize = 24.0f;
        activity.iterateViews(rootView, textSize);
        assertEquals(textSize, textView.getTextSize(), 0);
        assertEquals(textSize, editText.getTextSize(), 0);
        assertEquals(textSize, button.getTextSize(), 0);
        System.out.println("Test passed");
    }

    @Test
    public void testSetFontSize() {
        System.out.println("Testing testSetFontSize");
        TextView textView = new TextView(activity.getApplicationContext());
        ViewGroup rootView = new ViewGroup(activity.getApplicationContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {}
        };
        rootView.addView(textView);

        float textSize = 14.0f;
        activity.setFontSize(textSize);
        assertEquals(textSize, textView.getTextSize(), 0);
        System.out.println("Test passed");
    }

    @Test
    public void testSaveButton() {
        System.out.println("Testing testSaveButton()");
        Button savingit2 = activity.findViewById(R.id.saveIt2);
        assertNotNull(savingit2);

        savingit2.performClick();
        Intent expectedIntent = new Intent(activity, HomeScreen.class);
        Intent actualIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        System.out.println("Test passed");
    }

    @Test
    public void testFontSizeSlider() {
        System.out.println("Testing testFontSizeSlider()");
        FontSizeScreen fontSizeScreen = Robolectric.buildActivity(FontSizeScreen.class).create().get();
        SeekBar fontSizeSeekBar = fontSizeScreen.findViewById(R.id.fontSizeButton);
        TextView textView = fontSizeScreen.findViewById(R.id.textView);
        float initialSize = textView.getTextSize() / fontSizeScreen.getResources().getDisplayMetrics().scaledDensity;

        fontSizeSeekBar.setProgress(20);
        float newSize = fontSizeSeekBar.getProgress();
        fontSizeScreen.setFontSize(newSize);
        float updatedSize = textView.getTextSize() / fontSizeScreen.getResources().getDisplayMetrics().scaledDensity;

        assertEquals(newSize, updatedSize, 0.01f);

        fontSizeSeekBar.setProgress((int)initialSize);
        fontSizeScreen.setFontSize(initialSize);
        float resetSize = textView.getTextSize() / fontSizeScreen.getResources().getDisplayMetrics().scaledDensity;

        assertEquals(initialSize, resetSize, 0.01f);
        System.out.println("Test passed");
    }
}
