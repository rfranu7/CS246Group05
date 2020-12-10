package com.example.purpleinventoryapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** main page for inventory access.
 * @author Team-05
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "MAIN INVENTORY ACTIVITY";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        long expired = preferences.getLong("ExpiredDate", -1);
        if(expired > System.currentTimeMillis()){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("RanBefore");
            editor.commit();
        }
    }

    protected void onStart() {
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

        if(isFirstTime()){
            Inventory inventory = new Inventory(this);
            inventory.getLowInventory();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();

                // Delete shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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
        Intent intent = new Intent(this, CategoryList.class);
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

    /***
     * Checks that application runs first time and write flag at SharedPreferences
     * @return true if 1st time
     */
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60));
            editor.commit();
        }
        return !ranBefore;
    }
}