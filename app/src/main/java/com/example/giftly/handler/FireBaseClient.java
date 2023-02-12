package com.example.giftly.handler;

import static android.content.ContentValues.TAG;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.giftly.Giftly;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FireBaseClient {

    //method wrappers because typing out that class name annoys me
    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
    public  static FirebaseFirestore getDB() {
        return FirebaseFirestore.getInstance();
    }


    public static void createProfile(User newUser) {
        //create a Map with user data using firebase doc Schema
        Map<String, Object> user = new HashMap<>();
        user.put("Name", newUser.fullName);
        user.put("Events", newUser.events);
        user.put("Interests", newUser.interests);
        //reference the collection and call a set event using the authorized users ID
        if (FirebaseAuth.getInstance().getUid() != null)
            getDB().collection("Users").document(getAuth().getUid()).set(user);
    }

    //Reads a user from the database with the matching document ID
    public ListenableFuture<User> readUser(String UserID) {
        return Giftly.service.submit(new userCallback(UserID));
    }
    private class userCallback implements Callable<User> {
        String UserID;
        userCallback(String UID) {UserID = UID;}

        @Override
        public User call() {
            DocumentReference targetUser = getDB().collection("Users").document(UserID);

            //log status of document allocation
            Task<DocumentSnapshot> callDB = targetUser.get().addOnCompleteListener(task -> {
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
            }); //returns DocumentSnapshot

            try {
                Tasks.await(callDB);
                return new User(callDB.getResult());
            }
            catch (Exception e) {
                return null;
            }

        }

    }


    public static Event readEvent(String eventID) {

        DocumentReference targetEvent = getDB().collection("Users").document(eventID);
        DocumentSnapshot event = targetEvent.get().addOnCompleteListener(task -> {
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
        }).getResult();
        if (event.exists())
            return new Event(event);

        else return null;
    }
}
