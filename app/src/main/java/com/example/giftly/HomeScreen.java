package com.example.giftly;
import static android.content.ContentValues.TAG;

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

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //find Users name and display it to test readUser data
        TextView display = findViewById(R.id.TestDisplay);

        Futures.addCallback(
                Giftly.client.readUser(Giftly.client.getAuth().getUid()),
                new FutureCallback<User>() {
                    public void onSuccess(User result) {
                        // handle success
                        display.setText(result.getFullName());
                    }

                    public void onFailure(@NonNull Throwable thrown) {
                        // handle failure
                        //kill the app idk
                    }
                },
                // causes the callbacks to be executed on the main (UI) thread
                this.getMainExecutor()
        );


        //Load User Info
        //log status of document allocation


    }

}