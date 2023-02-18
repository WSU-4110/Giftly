package com.example.giftly;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class DisplayEventScreen extends AppCompatActivity {

    public Button button_edit_event;

    // Temporary List of Participants; array to be filled in by database info
    ListView list_1;
    String participants[]
            ={"Cayden", "Luis", "Matt", "Noura", "Hanna"};

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_screen);
        Intent eventIntent = getIntent();
        String eventID = eventIntent.getStringExtra("eventID");
        Log.d(TAG, "EventID: " + eventID);

        // This section is commented out until we add the edit event page?
//        button_edit_event = (Button) findViewById(R.id.edit_event);
//        button_edit_event.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DisplayEventScreen.this, edit_event_screen.class);
//                startActivity(intent);
//            }
//        });

        list_1.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        // The list of participants is displayed on the screen
        list_1 = findViewById(R.id.participant_list);
        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,participants);
        list_1.setAdapter(arr);
        list_1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    Intent i=new Intent(DisplayEventScreen.this,DisplayEventScreen.class);
                    startActivity(i);
                }
            }
        });
    }
}

