package com.example.giftly.handler;

import static android.content.ContentValues.TAG;
//import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class fireBaseClient extends User {

    FirebaseAuth authentication;
    FirebaseFirestore db;

    fireBaseClient(FirebaseAuth auth) {
        authentication = auth;
        db = FirebaseFirestore.getInstance();
    }

    void createProfile(User newUser) {
        //create a Map with user data using firebase doc Schema
        Map<String, Object> user = new HashMap<>();
        user.put("Name", newUser.fullName);
        user.put("Events", newUser.events);
        user.put("Interests", newUser.interests);
        //reference the collection and call a set event using the authorized users ID
        db.collection("Users").document(authentication.getUid()).set(user);
    }

    //Reads a user from the database with the matching document ID
    User readUser(String UserID) {
        //Create the reference in the users collection with the provided ID
        DocumentReference targetUser = db.collection("Users").document(UserID);
        //
        DocumentSnapshot user = targetUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //log status of document allocation
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                    }
                    else {
                        Log.d(TAG, "No Such Document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }
        }).getResult(); //returns DocumentSnapshot

        if (user.exists()) //If the Doc exists pass it to a User constructor
            return new User(user);

        else return null;
    }

    Event readEvent(String eventID) {

        DocumentReference targetEvent = db.collection("Users").document(eventID);
        DocumentSnapshot event = targetEvent.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                    }
                    else {
                        Log.d(TAG, "No Such Document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }
        }).getResult();

        if (event.exists())
            return new Event(event);

        else return null;
    }
}
