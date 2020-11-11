package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User implements FirestoreAccess {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    public String userId;
    public String emailAddress;
    public String name;
    public String companyId;
    Activity activity;


    User() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void createUser(String userId, String emailAddress, String name, String companyId) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.name = name;
        this.companyId = companyId;
    }

    public void setUserId(String userId, Activity activity) {
        this.userId = userId;
        this.activity = activity;
    }

    @Override
    public void writeData() {
        final String TAG = "Create User"; // TAG USED FOR LOGGING

        /*
            Create a new user:
            -
        */

        Map<String, Object> user = new HashMap<>();
        user.put("userId", this.userId);
        user.put("emailAddress", this.emailAddress);
        user.put("name", this.name);
        user.put("companyId", this.companyId);
        user.put("created",new Date().getTime());

        db.collection("users")
                .document(this.userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void getAllData() {
        return;
    }

    @Override
    public void getDataById() {
        final String TAG = "Display user"; // TAG USED FOR LOGGING

        /*
            Get specific user using the Id provided on Fire Store
        */

        DocumentReference docId = db.collection("users").document(this.userId);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        User.this.emailAddress = document.get("emailAddress").toString();
                        User.this.name = document.get("name").toString();
                        User.this.companyId = document.get("companyId").toString();

                        // Save the details on shared preferences for future calls
                        SharedPreferences sharedPreferences = User.this.activity.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", User.this.userId);
                        editor.putString("emailAddress", User.this.emailAddress);
                        editor.putString("name", User.this.name);
                        editor.putString("companyId", User.this.companyId);
                        editor.apply();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void updateDataById(Map<String, Object> updates) {

    }

    @Override
    public void deleteDataById() {

    }
}