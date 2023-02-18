package com.example.giftly;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class DisplayEventScreen extends AppCompatActivity {

    // Temporary List of Participants; array to be filled in by database info
    ListView list_1;
    String participants[]
            ={"Cayden", "Luis", "Matt", "Noura", "Hanna"};


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        // The list of participants is displayed on the screen
        list_1 = findViewById(R.id.participant_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,participants);
        list_1.setAdapter(arr);
    }
}

