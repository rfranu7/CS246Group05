package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        Company newCompany = new Company();
        newCompany.CompanyName("Purple Store");

        /*
            All Firestore connections are working
            Uncomment the methods for testing.
            Open firestore in Firebase console using the
            Username and Password on slack :D
        */

        // Testing Write Data
        //newCompany.writeData();

        /*
        // Testing Read Data
        newCompany.getAllData();
        newCompany.getDataById();
        */

        /*
        // Testing Update Data
        Map<String, Object> updates = new HashMap<>(); // Create object (dictionary) for updates list
        updates.put("businessName", "Violet Store"); // Add the field to be updated
        updates.put("businessOwner", "Baldwin Felipe"); // Adding new field sample
        newCompany.updateDataById(updates);
         */

        // Testing Delete Data
        //newCompany.deleteDataById();
    }
}