package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Randeep Ranu - rfranu7@gmail.com
 * This creates the expandable items from a given List<Map<String, Object>>
 */

public class ExpandableListDataPump {

    /**
     * Method to call to convert raw data from firestore to Expandable view
     * @param data
     * @return expandableListDetail (HashMap<String, List<String>>)
     */
    public static TreeMap<String, List<String>> getData(List<Map<String, Object>> data) {
        final String TAG = "GET DATA FOR TABLE";
        final TreeMap<String, List<String>> expandableListDetail = new TreeMap<String, List<String>>();

        Log.d(TAG, "Expandable List Data Started");

        for(int i=0; i < data.size(); i++) {
            String ID = data.get(i).get("ID").toString();
            String itemName = data.get(i).get("ItemName").toString();
            String quantity = data.get(i).get("itemQuantity").toString();
            String category = data.get(i).get("itemCategory").toString();
            String unit = data.get(i).get("itemUnit").toString();
            String cost = data.get(i).get("itemCost").toString();
            String price = data.get(i).get("itemPrice").toString();
            Log.d(TAG, itemName);

            List<String> item = new ArrayList<String>();
            item.add(ID);
            item.add("Quantity: "+quantity);
            item.add("Category: "+category);
            item.add("Unit: "+unit);
            item.add("Cost: "+cost);
            item.add("Price: "+price);

            expandableListDetail.put(itemName+" "+unit, item);
        }

        Log.d(TAG, "Done looping");

        Log.d(TAG, "returning list");
        Log.d(TAG, expandableListDetail.toString());
        return expandableListDetail;
    }
}
