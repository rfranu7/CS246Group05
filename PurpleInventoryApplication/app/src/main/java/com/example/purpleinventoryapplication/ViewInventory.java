package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.List;

public class ViewInventory extends AppCompatActivity {
    private final String TAG = "VIEW INVENTORY ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        Inventory data = new Inventory(this);
        data.getAllData();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_view_inventory, (List<String>) data);

        GridView gridView = (GridView) findViewById(R.id.inventoryTable);
        gridView.setAdapter(adapter);
    }
}