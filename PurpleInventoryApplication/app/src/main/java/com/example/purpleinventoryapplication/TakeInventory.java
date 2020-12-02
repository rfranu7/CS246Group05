package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_inventory);


        Inventory inventory = new Inventory(this);
        inventory.getAllData(new VolleyOnEventListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onSuccess(List response) {
                Log.d(TAG, "Getting data successful");

                List<Map<String, Object>> data = response;

                Log.d(TAG, data.toString());

                myListView = (ListView) findViewById(R.id.myListView);
                takeInventoryDetail = TakeInventoryDataPump.getData(data);
                takeInventoryAdapter = new TakeInventoryAdapter(this, takeInventoryDetail);
                myListView.setAdapter(takeInventoryAdapter);

            }
        });
    }
}