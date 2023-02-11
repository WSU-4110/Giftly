package com.example.giftly;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.giftly.handler.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //find Users name and display it to test readUser data
        TextView display = findViewById(R.id.TestDisplay);


        //Load User Info
        //log status of document allocation
        FireBaseClient.readUser(FireBaseClient.getAuth().getUid()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    //IF USER EXISTS, DISPLAY INFO?
                    User CurrentUser = new User(doc);
                    display.setText(CurrentUser.getFullName());
                }
                else { //IF NO RESULT, POP UP CREATE PROFILE?

                }
            }
            else {
                //WHEN TASK FAILS, KICK BACK TO SIGN UP?
            }
        });

    }

}