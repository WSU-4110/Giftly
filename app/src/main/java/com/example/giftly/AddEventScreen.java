package com.example.giftly;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

public class AddEventScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final Calendar myCalendar= Calendar.getInstance();
    EditText editText;

    public Button button_create_event, button_cancel_add;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        //Button Functionality
        button_create_event = (Button) findViewById(R.id.button_add_event);
        button_cancel_add = (Button) findViewById(R.id.button_cancel);
        button_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventScreen.this, DisplayEventScreen.class);
                startActivity(intent);
            }
        });

        button_cancel_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.event_type_selection);
        spinner.setOnItemSelectedListener(this);

        // Temporary array
        List<String> categories = new ArrayList<String>();
        categories.add("Choose Event Type");
        categories.add("Single Recipient");
        categories.add("Secret Santa");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        editText=(EditText) findViewById(R.id.enter_date);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEventScreen.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        ((TextView) parent.getChildAt(0)).setTextColor(Color.LTGRAY);
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



}