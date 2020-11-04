package com.example.purpleinventoryapplication;

import com.google.firebase.firestore.FirebaseFirestore;

public interface FirestoreAccess {
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void WriteData();


    public void ReadData();

}
