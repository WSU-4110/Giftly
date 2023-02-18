package com.example.giftly;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.giftly.handler.Event;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class crappyEventViewer extends AppCompatActivity {
    TextView displayTest = findViewById(R.id.testDisplay);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crappy_event_viewer);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");

        Futures.addCallback(
                Giftly.client.readEvent(eventID),
                new FutureCallback<Event>() {
                    @Override
                    public void onSuccess(Event result) {
                        runOnUiThread(new displayEvent(result.toString()));
                    }
                    @Override
                    public void onFailure(Throwable thrown) {
                    }
                }, Giftly.service
        );
    }

    class displayEvent implements Runnable {
        String display;
        displayEvent(String e) { display = e;}
        @Override
        public void run() {
            displayTest.setText(display);
        }
    };

}