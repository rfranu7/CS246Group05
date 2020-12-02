package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * @author Team-05
 *
 */
public class EditInventory extends AppCompatActivity {
    private final String TAG = "EDIT INVENTORY ACTIVITY";
    Inventory item;
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);

        itemId = getIntent().getStringExtra("itemId");
        Log.d("EDIT", itemId.toString());

        item = new Inventory(this);
        item.getDataById(itemId);
    }

    public void applyChanges(View view) {
        Log.d(TAG, "APPLY CHANGES");

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

        item.createItem(itemName, price, cost, quantity, unit, category);
        item.updateDataById(itemId);
    }
}