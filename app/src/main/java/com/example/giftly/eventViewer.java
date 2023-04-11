package com.example.giftly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.giftly.handler.IEvent;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class eventViewer extends AppCompatActivity {
    TextView displayTest = findViewById(R.id.testDisplay);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_viewer);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");

        Futures.addCallback(
                Giftly.client.readEvent(eventID),
                new FutureCallback<IEvent>() {
                    @Override
                    public void onSuccess(IEvent result) {
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