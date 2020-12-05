package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TakeInventory extends AppCompatActivity {
    private final String TAG = "TAKE INVENTORY ACTIVITY";

    ListView myListView;
    TakeInventoryAdapter takeInventoryAdapter;
    HashMap<String, List<String>> takeInventoryDetail;
    List<String> takeInventoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_inventory);

        String category = getIntent().getStringExtra("category");

        Inventory inventory = new Inventory(this);
        inventory.getDataByCategory(category, new VolleyOnEventListener() {
            @Override
            public void onSuccess(List response) {
                Log.d(TAG, "Getting data successful");

                final List<Map<String, Object>> data = response;

                myListView = (ListView) findViewById(R.id.myListView);
                myListView.setItemsCanFocus(true);
                takeInventoryDetail = TakeInventoryDataPump.getData(data);
                takeInventoryTitle = new ArrayList<String>(takeInventoryDetail.keySet());
                takeInventoryAdapter = new TakeInventoryAdapter(TakeInventory.this, takeInventoryTitle, takeInventoryDetail);
                myListView.setAdapter(takeInventoryAdapter);

                //final Button addButton = (Button) findViewById(R.id.addInventory);

                //final Object tag = addButton.getTag();

//                addButton.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        int quantity = (int) data.get((Integer) tag).get("itemQuantity");
//                        quantity += 1;
//
//                        String ID = (String) data.get((Integer) tag).get("ID");
//                        Inventory inventory = new Inventory(TakeInventory.this);
//                        Map<String, Object> addQuantity = new HashMap<>();
//                        addQuantity.put("itemQuantity", quantity);
//                        addQuantity.put("dateUpdated", new Date().getTime());
//                        inventory.updateDataById(ID, addQuantity);
//                    }
//                });
            }
        });
    }
}