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
        Map<String, Object> user = new HashMap<>();
        user.put("ParticipantsA", newUser.fullName);
        user.put("Events", newUser.events);
        user.put("Interests", newUser);

        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });



    }

    //t[] getUserEvents() {}



    User readProfile(String UserID) {

        DocumentReference targetUser = db.collection("Users").document(UserID);
        DocumentSnapshot user = targetUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        if (user.exists())
            return new User(user.getData());

        else return null;
    }
}
