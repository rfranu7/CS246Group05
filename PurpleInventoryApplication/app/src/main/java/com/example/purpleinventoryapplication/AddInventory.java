package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class AddInventory extends AppCompatActivity implements LifecycleObserver {

    private static final String APP_PREFS = "TEMPORARY_FORM_APPLICATION_PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        EditText categoryField = (EditText) findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        String itemName = nameField.getText().toString();
        String quantity = quantityField.getText().toString();
        String unit = unitField.getText().toString();
        String category = categoryField.getText().toString();
        String price = priceField.getText().toString();
        String cost = costField.getText().toString();

        SharedPreferences sharedPref = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ITEM_NAME", itemName);
        editor.putString("ITEM_QUANTITY", quantity);
        editor.putString("ITEM_UNIT", unit);
        editor.putString("ITEM_CATEGORY", category);
        editor.putString("ITEM_PRICE", price);
        editor.putString("ITEM_COST", cost);
        editor.apply();


        Log.d("ON PAUSE", sharedPref.toString());
        Log.d("ON PAUSE", "PAUSED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);

        Log.d("ON RESUME", sharedPreferences.toString());
        Log.d("ON RESUME", "RESUMED");

        String itemName = sharedPreferences.getString("ITEM_NAME", null);
        String quantity = sharedPreferences.getString("ITEM_QUANTITY", null);
        String unit = sharedPreferences.getString("ITEM_UNIT", null);
        String category = sharedPreferences.getString("ITEM_CATEGORY", null);
        String price = sharedPreferences.getString("ITEM_PRICE", null);
        String cost = sharedPreferences.getString("ITEM_COST", null);

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        EditText categoryField = (EditText) findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        nameField.setText(itemName);
        quantityField.setText(quantity);
        unitField.setText(unit);
        categoryField.setText(category);
        priceField.setText(price);
        costField.setText(cost);
    }
}