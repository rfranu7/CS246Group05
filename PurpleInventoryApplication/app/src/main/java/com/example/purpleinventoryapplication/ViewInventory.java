package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewInventory extends AppCompatActivity {
    private final String TAG = "VIEW INVENTORY ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
    }
}