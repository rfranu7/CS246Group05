package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.firestore.v1.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * allows user to see all items in table format using GridView adapter
 */
public class ViewInventory extends AppCompatActivity {
    private final String TAG = "VIEW INVENTORY ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        Inventory inventory = new Inventory(this);
        inventory.getAllData();

        List<Map<String, Object>> data = inventory.itemList;
        Log.d(TAG, "onCreate: " + data);

//        for (Map.Entry<String, Object> entry : data[0].entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            Log.i(TAG, "onCreate: " + key + " " + value);
            // do stuff
        //}


//        Set keys = data.keySet();
//
//        for (Iterator i = keys.iterator(); i.hasNext(); ) {
//            String key = (String) i.next();
//            String value = (String) map.get(key);


          // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_view_inventory, data);

//        GridView gridView = (GridView) findViewById(R.id.inventoryTable);
//        gridView.setAdapter(adapter);
    }
}