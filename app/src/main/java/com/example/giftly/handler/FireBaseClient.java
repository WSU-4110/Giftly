package com.example.giftly.handler;

//all of the beautiful imports
import static android.content.ContentValues.TAG;
import static com.example.giftly.Giftly.service;

import android.util.Log;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class FireBaseClient {

    //method wrappers because typing out that class name annoys me
    public FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
    public FirebaseFirestore getUser() {
        return FirebaseFirestore.getInstance();
    }
    //Private instance object and constructor
    private static FireBaseClient instance;
    private FireBaseClient() {}
    //Public instance accessor
    public static FireBaseClient getClient() {
        if (instance == null)
            instance = new FireBaseClient();
        return instance;
    }


    //sets firebase user doc corresponding to current Auth login to user
    public void createProfile(User newUser) {
        //create a Map with user data using firebase doc Schema
        Map<String, Object> user = new HashMap<>();
        user.put("Name", newUser.fullName);
        user.put("Events", newUser.events);
        user.put("Interests", newUser.interests);
        //reference the collection and call a set event using the authorized users ID
        if (getAuth().getUid() != null)
            getUser().collection("Users").document(getAuth().getUid()).set(user);
    }

    //Sets the corresponding element of the array in firebase to the provided string
    public ListenableFuture<String> setGift(String targetUserID, String eventID, String gift) {
        class updateGiftList implements Callable<String> {
            @Override
            public String call() throws Exception {
                DocumentReference targetEvent = getUser().collection("Events").document(eventID);
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
                    ArrayList<String> giftList;
                    ArrayList<String> participants = (ArrayList<String>)event.get("participants");
                    //find the index of the current user in the event participants list
                    assert participants != null;
                    int userIndex = participants.indexOf(getAuth().getUid());
                    if (event.contains(targetUserID)) {
                        giftList = (ArrayList<String>)event.get(targetUserID);
                        //check if the array is currently updated to handle the index of the user
                        assert giftList != null;
                        giftList.ensureCapacity(userIndex);
                    }
                    else {
                        giftList = new ArrayList<>(userIndex+1);
                    }
                    giftList.set(userIndex,gift.trim());
                    getUser().collection("Events").document(eventID).update(targetUserID, giftList);
                }
                catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                    return "Update Event Failed";
                }
                return "Updated gift list success";
            }
        }


        return service.submit(new updateGiftList());
    }

    public ListenableFuture<String> joinEvent(String eventID) {
        return service.submit(new joinEventRequest(eventID));
    }
    private class joinEventRequest implements Callable<String> {
        String eventID;
        public joinEventRequest(String eventID) {
            this.eventID = eventID.trim();
        }

        @Override
        public String call() throws Exception {
            DocumentReference targetEvent = getUser().collection("Events").document(eventID);
            DocumentReference targetUser = getUser().collection("Users").document(Objects.requireNonNull(getAuth().getUid()));
            //log status of document allocation
            Task<DocumentSnapshot> getEvent = targetEvent.get().addOnCompleteListener(task -> {
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
            }); //returns Event Snapshot

            Task<DocumentSnapshot> getUser = targetUser.get().addOnCompleteListener(task -> {
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
            }); //returns User Snapshot

            //wait for task completion
            Tasks.await(getEvent);
            Tasks.await(getUser);

            DocumentSnapshot user = getUser.getResult();
            DocumentSnapshot eventSnapshot = getUser.getResult();

            if (eventSnapshot.exists() && getUser.isSuccessful() ) {

                IEvent event = EventBuilder.createEvent(eventSnapshot);
                event.addParticipant(getAuth().getUid());
                targetEvent.update("participants", event.getParticipants());

                //Check if user is already a part of the event, if not add them and update the doc
                Log.d(TAG, "Checking User");
                ArrayList<String> eventList = new ArrayList<>(1);
                if (user.exists() && user.get("Events") != null)
                    eventList = (ArrayList<String>) user.get("Events");
                Log.d(TAG, eventList.toString());
                if (!eventList.contains(eventID))
                    eventList.add(eventID);
                    Task<Void> updateUser =  targetUser.update("Events", eventList);


                Tasks.await(updateUser);
                if (updateUser.isSuccessful()) return "Successfully Joined event";
                else throw new Exception("Join Event Failed, please try again");
            }
            else throw new Exception("Event Not Found");
        }
    };

    public ListenableFuture<String> createEvent(Map<String, Object> eventMap) {
        return service.submit(new createEventRequest(eventMap));
    }
    //Non-Blocking Event Creation Request
    private class createEventRequest implements Callable<String> {
    Map<String, Object> eventMap;

        public createEventRequest(Map<String, Object> eventMap) {
            this.eventMap = eventMap;
        }

        @Override
        public String call() throws Exception {
            Log.d(TAG, "Event Creation Request started");
            ArrayList<String> participants = new ArrayList<>(1);
            participants.add(getAuth().getUid()); //
            //get basic event data
            eventMap.put("eventOwner", getAuth().getUid());  //sets the owner to the creator
            eventMap.put("participants", participants);  //adds an array list with just the event creator in it

            DocumentReference targetUser = getUser().collection("Users").document(Objects.requireNonNull(getAuth().getUid()));


            Log.d(TAG, "Retrieval Tasks started");
            //log status of document allocation
            Task<DocumentReference> getEvent =  getUser().collection("Events").add(eventMap).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentReference doc = task.getResult();
                }
                else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }); //returns Event Snapshot
            Task<DocumentSnapshot> getUser = targetUser.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Log.d(TAG, "User DocumentSnapshot data: " + doc.getData());
                    }
                    else {
                        Log.d(TAG, "No Such Document");
                    }
                }
                else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }); //returns User Snapshot

            try {
                //wait for task completion
                Log.d(TAG, "Waiting for Event");
                Tasks.await(getEvent);
                Log.d(TAG, "Waiting for User");
                Tasks.await(getUser);

                if (getEvent.isSuccessful() && getUser.isSuccessful()) {
                    ArrayList<String> events = (ArrayList<String>)getUser.getResult().getData().get("Events");
                    assert events != null;
                    events.add(getEvent.getResult().getId());
                    targetUser.update("Events", events);
                    return getEvent.getResult().getId();
                }
                else return "Failed to create Document.";

            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
                return "Update Event Failed";
            }
        }
    };

    //Reads a user from the database with the matching document ID and returns Listenable Future for a user
    public ListenableFuture<User> readUser(String UserID) {
        return service.submit(new userCallback(UserID));
    }
    //callable class that makes a request to FireBase and constructs a user when it gets a response, fulfilling the future
    private class userCallback implements Callable<User> {
        String UserID;
        userCallback(String UID) {UserID = UID;}
        @Override
        public User call() {
            DocumentReference targetUser = getUser().collection("Users").document(UserID);

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
        return service.submit(new userListCallback(UserIDs));
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

            Task<QuerySnapshot> callDB = getUser().collection("Users").whereIn(FieldPath.documentId(), UserIDList).get().addOnCompleteListener(task -> {
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
    public ListenableFuture<IEvent> readEvent(String eventID) {
        return service.submit(new eventCallback(eventID));
    }
    //Callable implemented class that returns a Future
    private class eventCallback implements Callable<IEvent> {
        String eventID;
        eventCallback(String EID) {eventID = EID;}

        public IEvent call() {
            DocumentReference targetEvent = getUser().collection("Events").document(eventID);

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
                return EventBuilder.createEvent(callDB.getResult());
            }
            catch (Exception e) {
                Log.d(TAG, e.toString());
                return null;
            }
        }
    }

    //Reads a event from the database with the matching document ID and returns Listenable Future for a user
    public ListenableFuture<ArrayList<IEvent>> readEvent(ArrayList<String> eventIDs) {
        return service.submit(new eventListCallback(eventIDs));
    }
    //callable class that makes a request to FireBase and constructs a user when it gets a response, fulfilling the future
    private class eventListCallback implements Callable<ArrayList<IEvent>> {
        ArrayList<String> EventIDList;
        eventListCallback(ArrayList<String> EIDs) {
            EventIDList = EIDs;
        }

        @Override
        public ArrayList<IEvent> call() {
            ArrayList<IEvent> retrievedEvents = new ArrayList<>(EventIDList.size());
            Task<QuerySnapshot> callDB = getUser().collection("Events").whereIn(FieldPath.documentId(), EventIDList).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot docs = task.getResult();
                    if (docs.size() > 0) {
                        Log.d(TAG, "Retrieved " + docs.size() + " documents from firebase");
                    } else {
                        Log.d(TAG, "No Documents Retrieved");
                    }
                } else {
                    Log.d(TAG, "get failed with" + task.getException());
                }
            }); //returns DocumentSnapshot

            try {
                Tasks.await(callDB);
                for (DocumentSnapshot event : callDB.getResult()) {
                    Log.d(TAG, event.toString());
                    retrievedEvents.add(EventBuilder.createEvent(event));
                    Log.d(TAG, retrievedEvents.get(retrievedEvents.size()-1).toString());
                }
                Log.d(TAG, "Passing " + retrievedEvents.size() + " events to UI.");
                return retrievedEvents;
            }
            //Catches firebase exceptions when retrieving data from the snapshots
            catch (ExecutionException | InterruptedException e) {
                Log.d(TAG, "ERROR MAKING EVENT LIST: " + e);
                return null;
            }
        }
    }

    //Call event from firebase DB with eid
    public ListenableFuture<String> readGiftList(String eventID, String userID) {
        return service.submit(new giftListCallback(eventID, userID));
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

            DocumentReference targetEvent = getUser().collection("Events").document(eventID);

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
                for (String entry : (ArrayList<String>) Objects.requireNonNull(event.get(userID))) {
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