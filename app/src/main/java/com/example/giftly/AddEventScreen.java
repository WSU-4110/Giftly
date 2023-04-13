package com.example.giftly;

import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.example.giftly.handler.Event;
import com.example.giftly.handler.User;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final Calendar myCalendar= Calendar.getInstance();

    public Button button_create_event, button_cancel_adding;
    public TextView textbox_eventName, textbox_eventDate, textbox_eventLocation;
    private SharedPreferences sharedPreferences;
    public SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);


    public void onCreate(Bundle savedInstanceState) {

        //Grab Event Intent if it exists

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        // Testing Geocoding Functionality
        String Address = "Wayne State University";
        //String test = "https://api.mapbox.com/search/geocode/v6/forward?q="+Address;


        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .query(Address)
                .build();

        TextView tv1 = (TextView)findViewById(R.id.testAddress);

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();


                if (results.size() > 0) {

                    // Log the first results Point.
                    Point firstResultPoint = results.get(0).center();
                    Log.d(TAG, "LONGITUDE: " + firstResultPoint.longitude());
                    Log.d(TAG, "LATITUDE: " + firstResultPoint.latitude());
                    String lon = String.valueOf(firstResultPoint.longitude());
                    String lat = String.valueOf(firstResultPoint.longitude());
                    tv1.setText(lon);

                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");

                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        //Theme: Fetch the current color of the background
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedColor = sharedPreferences.getInt("BackgroundColor", ContextCompat.getColor(AddEventScreen.this, R.color.Default_color));
        getWindow().getDecorView().setBackgroundColor(savedColor);

        //Enable the back-button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Button Functionality
        button_create_event = (Button) findViewById(R.id.button_add_event);
        button_cancel_adding = (Button) findViewById(R.id.button_cancel);

        //Text Entries
        textbox_eventName = (EditText) findViewById(R.id.event_name_entry);
        textbox_eventDate = (EditText) findViewById(R.id.enter_date);
        textbox_eventLocation = (EditText) findViewById(R.id.locatiopn_entry);

        //Event Type Selector
        Spinner spinner = (Spinner)findViewById(R.id.event_type_selection);
        spinner.setOnItemSelectedListener(this);

        button_cancel_adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEventScreen.this, HomeScreen.class);
                startActivity(intent);
            }
        });
        button_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date eventDate;
                try {
                    eventDate = dateFormatter.parse(textbox_eventDate.getText().toString());
                    Log.d(TAG, "event date found");
                }
                catch (java.text.ParseException e) {
                    Log.d(TAG, e.getMessage());
                    eventDate = new Date();
                }

                HashMap<String, Object> eventMap = new HashMap<>(4);
                eventMap.put("eventStartDate", eventDate);
                eventMap.put("eventName", textbox_eventName.getText().toString());
                eventMap.put("eventType", spinner.getSelectedItemPosition());
                //eventMap.put("eventLocation",textbox_eventLocation.getText().toString());


                Futures.addCallback(
                        client.createEvent(eventMap),
                        new FutureCallback<String>() {

                            @Override
                            public void onSuccess(String eventID) {
                                Log.d(TAG, "Successfully Created Event");
                                Intent intent = new Intent(AddEventScreen.this, DisplayEventScreen.class);
                                intent.putExtra("eventID",eventID);
                                intent.putExtra("new",true);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Throwable thrown) {
                                runOnUiThread(new makeToast(thrown.getMessage()));
                                Log.d(TAG, "Add event failed: "+ thrown.getMessage());

                            }
                        }, Giftly.service);
            }
        });



        //TODO UNCOMMENT SPINNER CATEGOREY ADDITIONS AS THEY ARE IMPLEMENTED

        // Temporary array
        List<String> categories = new ArrayList<>();
        categories.add("Group Giving");
        //categories.add("Single Recipient");
        //categories.add("Secret Santa");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        textbox_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEventScreen.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void updateLabel(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy", Locale.US);
        textbox_eventDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        ((TextView) parent.getChildAt(0)).setTextColor(Color.LTGRAY);
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    //Back button configuration
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class makeToast implements Runnable {
        String message;
        makeToast(String s) {
            message = s;
        }
        @Override
        public void run() {
            Toast.makeText(AddEventScreen.this, message, Toast.LENGTH_SHORT).show();
        }
    }

}