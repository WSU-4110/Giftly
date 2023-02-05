package com.example.giftly.handler;
import static android.content.ContentValues.TAG;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.*;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class fireBaseClient extends User {
    String auth;
    FirebaseFirestore db;

    fireBaseClient() {
        db = FirebaseFirestore.getInstance();

    }

    void createProfile(User newUser) {
        Map<String, Object> user = new HashMap<>();
        user.put("profile", newUser);

        db.collection("users")
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


}
