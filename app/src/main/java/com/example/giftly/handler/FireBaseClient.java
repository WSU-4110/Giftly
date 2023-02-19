package com.example.giftly.handler;

//all of by beautiful imports
import static android.content.ContentValues.TAG;
import android.util.Log;

import com.example.giftly.Giftly;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class FireBaseClient {
    //method wrappers because typing out that class name annoys me
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
    public FirebaseFirestore getDB() {
        return FirebaseFirestore.getInstance();
    }

    public void createProfile(User newUser) {
        //create a Map with user data using firebase doc Schema
        Map<String, Object> user = new HashMap<>();
        user.put("Name", newUser.fullName);
        user.put("Events", newUser.events);
        user.put("Interests", newUser.interests);
        //reference the collection and call a set event using the authorized users ID
        if (getAuth().getUid() != null)
            getDB().collection("Users").document(getAuth().getUid()).set(user);
    }

    //Reads a user from the database with the matching document ID and returns Listenable Future for a user
    public ListenableFuture<User> readUser(String UserID) {
        return Giftly.service.submit(new userCallback(UserID));
    }
    //callable class that makes a request to FireBase and constructs a user when it gets a response, fulfilling the future
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

    //Reads a user from the database with the matching document ID and returns Listenable Future for a user
    public ListenableFuture<ArrayList<User>> readUser(ArrayList<String> UserIDs) {
        return Giftly.service.submit(new userListCallback(UserIDs));
    }
    //callable class that makes a request to FireBase and constructs a user when it gets a response, fulfilling the future
    private class userListCallback implements Callable<ArrayList<User>> {
        ArrayList<String> UserIDList;

        userListCallback(ArrayList<String> UIDs) {
            UserIDList = UIDs;
        }

        @Override
        public ArrayList<User> call() {
            ArrayList<User> retrievedUsers = new ArrayList<>(UserIDList.size());

            Task<QuerySnapshot> callDB = getDB().collection("Users").whereIn(FieldPath.documentId(), UserIDList).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot docs = task.getResult();
                    if (docs.size() > 0) {
                        Log.d(TAG, "Retrieved " + docs.size() + " from firebase");
                    } else {
                        Log.d(TAG, "No Documents Retrieved");
                    }
                } else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }); //returns DocumentSnapshot

            try {
                Tasks.await(callDB);
                for (DocumentSnapshot user : callDB.getResult())
                    retrievedUsers.add(new User(user));
                return retrievedUsers;
            } catch (Exception e) {
                return null;
            }
        }
    }

    //Call event from firebase DB with eid
    public ListenableFuture<Event> readEvent(String eventID) {
        return Giftly.service.submit(new eventCallback(eventID));
    }
    //Callable implemented class that returns a Future
    private class eventCallback implements Callable<Event> {
        String eventID;
        eventCallback(String EID) {eventID = EID;}

        public Event call() {
            DocumentReference targetEvent = getDB().collection("Events").document(eventID);

            //log status of document allocation
            Task<DocumentSnapshot> callDB = targetEvent.get().addOnCompleteListener(task -> {
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
                return new Event(callDB.getResult());
            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
                return null;
            }
        }
    }

    //Reads a event from the database with the matching document ID and returns Listenable Future for a user
    public ListenableFuture<ArrayList<Event>> readEvent(ArrayList<String> eventIDs) {
        return Giftly.service.submit(new eventListCallback(eventIDs));
    }
    //callable class that makes a request to FireBase and constructs a user when it gets a response, fulfilling the future
    private class eventListCallback implements Callable<ArrayList<Event>> {
        ArrayList<String> EventIDList;

        eventListCallback(ArrayList<String> EIDs) {
            EventIDList = EIDs;
        }

        @Override
        public ArrayList<Event> call() {
            ArrayList<Event> retrievedUsers = new ArrayList<>(EventIDList.size());

            Task<QuerySnapshot> callDB = getDB().collection("Events").whereIn(FieldPath.documentId(), EventIDList).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot docs = task.getResult();
                    if (docs.size() > 0) {
                        Log.d(TAG, "Retrieved " + docs.size() + " from firebase");
                    } else {
                        Log.d(TAG, "No Documents Retrieved");
                    }
                } else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }); //returns DocumentSnapshot

            try {
                Tasks.await(callDB);
                for (DocumentSnapshot event : callDB.getResult())
                    retrievedUsers.add(new Event(event));
                return retrievedUsers;
            } catch (Exception e) {
                Log.d(TAG, "ERROR MAKING EVENT LIST: " + e);
                return null;
            }
        }
    }

    //Call event from firebase DB with eid
    public ListenableFuture<String> readGiftList(String eventID, String userID) {
        return Giftly.service.submit(new giftListCallback(eventID, userID));
    }
    //Callable implemented class that returns a Future
    private class giftListCallback implements Callable<String> {
        String eventID;
        String userID;

        public giftListCallback(String eventID, String userID) {
            this.eventID = eventID;
            this.userID = userID;
        }

        public String call() {
            StringBuilder giftList = new StringBuilder();

            DocumentReference targetEvent = getDB().collection("Events").document(eventID);

            //log status of document allocation
            Task<DocumentSnapshot> callDB = targetEvent.get().addOnCompleteListener(task -> {
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
                DocumentSnapshot event = callDB.getResult();

                Log.d(TAG, userID + ": " + event.contains(userID));
                for (String entry : (ArrayList<String>)event.get(userID)) {
                    giftList.append(entry).append("\n");
                }
                return giftList.toString();
            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
                return "No Gifts Found";
            }
        }
    }

}
