package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class EditInventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);

        Inventory item = new Inventory(this);
        item.getDataById("3JP7WiAMcx0xMQBUyYKg");
    }

    public void applyChanges(View view) {

    }
}