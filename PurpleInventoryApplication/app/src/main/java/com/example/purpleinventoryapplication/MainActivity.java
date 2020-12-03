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

/** main page for inventory access.
 * @author Team-05
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
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

            if(userId == null || userId != uid || emailAddress == null || name == null || companyId == null) {
                // If any of the three is null, get user details from Fire Store
                User currUser = new User();
                currUser.setUserId(uid, this);
                currUser.getDataById();
            }
        }
    }

    /**
     * Creates addInventoryItem intent.
     * @param view
     */
    public void addInventoryItem(View view) {
        Intent intent = new Intent(this, AddInventory.class);
        startActivity(intent);
    }
    /**
     * Creates sendReportActivity intent.
     * @param view
     */
    public void sendReportActivity(View view) {
        Intent intent = new Intent(this, SendReport.class);
        startActivity(intent);
    }
    /**
     * Creates pointOfSaleActivity intent.
     * @param view
     */
    public void takeInventoryActivity(View view) {
        Intent intent = new Intent(this, TakeInventory.class);
        startActivity(intent);
    }
    /**
     * Creates viewInventoryActivity intent.
     * @param view
     */
    public void viewInventoryActivity(View view) {
        Intent intent = new Intent(this, ViewInventory.class);
        startActivity(intent);
    }

    /**
     * Deletes shared preferences.
     * @param view
     */
    public void logout(View view) {
        mAuth.signOut();

        // Delete shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}