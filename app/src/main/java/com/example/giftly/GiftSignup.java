package com.example.giftly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GiftSignup extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText editText;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_new_event);
        super.onCreate(savedInstanceState);


        Intent participantIntent = getIntent();
        String eventID = participantIntent.getStringExtra("userID");
        String userID = participantIntent.getStringExtra("eventID");




    }
}