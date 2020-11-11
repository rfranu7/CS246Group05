package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        // FirebaseApp.initializeApp(this);

        // Company newCompany = new Company();
        // newCompany.CompanyName("Purple Store");

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

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and redirects accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            String uid = currentUser.getUid();

            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", null);
            String emailAddress = sharedPreferences.getString("emailAddress", null);
            String name = sharedPreferences.getString("name", null);
            String companyId = sharedPreferences.getString("companyId", null);

            if(!userId.equals(uid) || emailAddress == null || name == null || companyId == null) {
                // If any of the three is null, get user details from Fire Store
                User currUser = new User();
                currUser.setUserId(uid, this);
                currUser.getDataById();
            }
        }
    }

    public void addInventoryItem(View view) {
        Intent intent = new Intent(this, AddInventory.class);
        startActivity(intent);
    }

    public void sendReportActivity(View view) {
        Intent intent = new Intent(this, SendReport.class);
        startActivity(intent);
    }

    public void pointOfSaleActivity(View view) {
        Intent intent = new Intent(this, Sales.class);
        startActivity(intent);
    }
    public void viewInventoryActivity(View view) {
        Intent intent = new Intent(this, ViewInventory.class);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();

        // Delete shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}