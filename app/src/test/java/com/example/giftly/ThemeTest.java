package com.example.giftly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.giftly.handler.EventBuilder;
import com.example.giftly.handler.GiftNetworkEvent;
import com.example.giftly.handler.IEvent;
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.CheckReturnValue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class ThemeTest {

    private CThemeScreen test;
    private SharedPreferences sharedPreferences;

    private Button dftThemeBtn;

    private ActionBar actionBar;
    @Before
    public void setUp() {

            test = Robolectric.buildActivity(CThemeScreen.class).create().resume().get();
            dftThemeBtn = test.findViewById(R.id.defaultTheme);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(test);
            actionBar = test.getSupportActionBar();
    }

    @After
    public void tearDown(){

        test.finish();

    }


    //Test that EventBuilder creates the correct class
    @Test
    public void testSaveButton() {


        System.out.println("Testing testSaveButton()");
        Button savingit = test.findViewById(R.id.saveIt);
        assertNotNull(savingit);

        savingit.performClick();
        Intent expectedIntent = new Intent(test, HomeScreen.class);
        Intent actualIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        System.out.println("Test passed for save button.");

    }

   @Test
    public void testOnThemeChanged() {

        //Set initial background color to the default color
        test.getWindow().getDecorView().setBackgroundColor(R.color.Default_color);

        //Call onThemeChanged with light gray color
        test.onThemeChanged(R.color.light_gray);


        Drawable backgroundDrawable = test.getWindow().getDecorView().getBackground();
        int actualColor = ((ColorDrawable) backgroundDrawable).getColor();
        assertEquals(R.color.light_gray, actualColor);
        System.out.println("Test passed for changing theme.");

   }

    @Test
    public void testDefaultThemeButton() {

        Button defaultBtn = test.findViewById(R.id.defaultTheme);
        assertNotNull(defaultBtn);
        defaultBtn.performClick();

        int expected = ContextCompat.getColor(test,R.id.defaultTheme);
        int actual = sharedPreferences.getInt("Background",0);
        assertEquals(expected,actual);
        System.out.println("Test passed for default theme button.");


    }

    @Test
    public void testLightThemeButton() {

        Button defaultBtn = test.findViewById(R.id.lightTheme);
        assertNotNull(defaultBtn);
        defaultBtn.performClick();

        int expected = ContextCompat.getColor(test,R.id.lightTheme);
        int actual = sharedPreferences.getInt("Background",0);
        assertEquals(expected,actual);
        System.out.println("Test passed for light theme button.");


    }
    @Test
    public void testIterateViews() {
        System.out.println("Testing testIterateViews");
        TextView textView = new TextView(test.getApplicationContext());
        EditText editText = new EditText(test.getApplicationContext());
        Button button = new Button(test.getApplicationContext());
        ViewGroup rootView = new ViewGroup(test.getApplicationContext()) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {}
        };
        rootView.addView(textView);
        rootView.addView(editText);
        rootView.addView(button);

        float textSize = 24.0f;
        test.iterateViews(rootView, textSize);
        assertEquals(textSize, textView.getTextSize(), 0);
        assertEquals(textSize, editText.getTextSize(), 0);
        assertEquals(textSize, button.getTextSize(), 0);
        System.out.println("Test passed for IterateViews.");
    }
    @Test
    public void testBackButton(){

        // Create an instance of ActionBar
        ActionBar actionBar = Robolectric.setupActivity(AppCompatActivity.class).getSupportActionBar();


        actionBar.setDisplayHomeAsUpEnabled(false);
        boolean expected = false;

        boolean actualValue = actionBar.closeOptionsMenu();
        assertEquals(expected,actualValue);
        System.out.println("Test passed for Back Button.");

    }

}
