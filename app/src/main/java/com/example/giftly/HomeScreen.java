package com.example.giftly;
import static android.content.ContentValues.TAG;

import static com.example.giftly.Giftly.client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.giftly.handler.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentSnapshot;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //find Users name and display it to test readUser data
        TextView display = findViewById(R.id.TestDisplay);

        //This call will display the user's name in the greeting message
        Futures.addCallback(
                Giftly.client.readUser(client.getAuth().getUid()),
                new FutureCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        display.setText(result.getFullName().toString());
                    }

                    @Override
                    public void onFailure(Throwable thrown) {

                    }
                },
                this.getMainExecutor()
        );

        //This call will display the events from the user
        /*
        Futures.addCallback(
                Giftly.client.readEvent("XoJlaxyaeUf2vqxn4R5h"),
                new FutureCallback<Event>() {
                    public void onSuccess(Event result) {
                        // handle success
                        display.setText(result.getEventName());
                    }

                    public void onFailure(@NonNull Throwable thrown) {
                        // handle failure
                        //kill the app idk
                    }
                },
                // causes the callbacks to be executed on the main (UI) thread
                this.getMainExecutor()
        );*/


        //Load User Info
        //log status of document allocation


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_profile){
            startActivity(new Intent(HomeScreen.this, ProfileScreen.class));
        }
        return super.onOptionsItemSelected(item);
    }
}