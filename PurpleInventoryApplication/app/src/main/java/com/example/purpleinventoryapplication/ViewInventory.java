package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firestore.v1.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Brown, Taelor McBride, Randeep Ranu
 * allows user to see all items in table format using GridView adapter
 */
public class ViewInventory extends AppCompatActivity {
    private final String TAG = "VIEW INVENTORY ACTIVITY";

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        Inventory inventory = new Inventory(this);
        inventory.getAllData(new VolleyOnEventListener() {
            @Override
            public void onSuccess(List response) {
                Log.d(TAG, "Getting data successful");

                List<Map<String, Object>> data = response;

                Log.d(TAG, data.toString());

                expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
                expandableListDetail = ExpandableListDataPump.getData(data);
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new CustomExpandableListAdapter(ViewInventory.this, expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
            }
        });
    }

    /**
     * Creates editInventory intent.
     * @param view
     */
    public void editInventory(View view) {
        Button editBtn = (Button) view.findViewById(R.id.buttonEdit);
        Object tag = editBtn.getTag();
        Log.d(TAG, tag.toString());
        Intent intent = new Intent(ViewInventory.this, EditInventory.class);
        intent.putExtra("itemId", tag.toString());
        startActivity(intent);
    }

    /**
     * Creates deleteInventory intent.
     * @param view
     */
    public void deleteInventory(View view) {
        Button delBtn = (Button) view.findViewById(R.id.buttonDelete);
        Object tag = delBtn.getTag();
        Inventory item = new Inventory(this);
        item.getDataName(tag.toString());
    }
}