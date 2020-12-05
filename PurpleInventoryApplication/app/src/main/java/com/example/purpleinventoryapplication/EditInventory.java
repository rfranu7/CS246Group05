package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Team-05
 *
 */
public class EditInventory extends AppCompatActivity {
    private final String TAG = "EDIT INVENTORY ACTIVITY";
    Inventory item;
    String itemId;
    HashMap<String, List<String>> categoryInfo;
    List<String> categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);

        final Spinner dropdown = findViewById(R.id.category);

        Category categories = new Category(this);

        categories.getAllData(new VolleyOnEventListener() {
            @Override
            public void onSuccess(List response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, "Getting data successful");

                List<Map<String, Object>> data = response;
                categoryInfo = new HashMap<String, List<String>>();

                for(int i=0; i < data.size(); i++) {
                    String name = data.get(i).get("categoryName").toString();
                    String created = data.get(i).get("created").toString();

                    List<String> detail = new ArrayList<String>();
                    detail.add(name);
                    detail.add(created);
                    categoryInfo.put(name, detail);
                }

                categoryName = new ArrayList<>(categoryInfo.keySet());
                Log.d(TAG, categoryName.toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditInventory.this, android.R.layout.simple_spinner_dropdown_item, categoryName);

                dropdown.setAdapter(adapter);
            }

        });

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
        Spinner categoryField = findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        String itemName = nameField.getText().toString();
        String quantity = quantityField.getText().toString();
        String unit = unitField.getText().toString();
        String category = categoryField.getSelectedItem().toString();
        String price = priceField.getText().toString();
        String cost = costField.getText().toString();

        Map<String, Object> updates = new HashMap<>();
        updates.put("ItemName", itemName);
        updates.put("itemPrice", price);
        updates.put("itemCost", cost);
        updates.put("itemQuantity", quantity);
        updates.put("itemUnit", unit);
        updates.put("itemCategory", category);
        //updates.put("itemImage", this.itemImage);
        updates.put("dateUpdated", new Date().getTime());

        item.updateDataById(itemId, updates, "view");
    }
}