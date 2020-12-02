package com.example.purpleinventoryapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TakeInventoryAdapter extends ArrayAdapter<Map<String, List<String>>> {
    private final String TAG = "TAKE INVENTORY ADAPTER";
    public String name;
    public String [] itemData;
    public String ID;
    public String quantity;


    public TakeInventoryAdapter(@NonNull VolleyOnEventListener context, Map<String, List<String>> Items) {
        super((Context) context, R.layout.activity_take_inventory, (List<Map<String, List<String>>>) Items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View CustomView = myInflater.inflate(R.layout.activity_take_inventory, parent, false);

        Map<String, List<String>> singleItem = getItem(position);

        for (String key : singleItem.keySet()) {
            name = key;
            List data = singleItem.get(key);
            ID = (String) data.get(0);
            quantity = (String) data.get(1);
        }

        TextView itemText = (TextView) CustomView.findViewById(R.id.itemText);
        EditText editQuantity = (EditText) CustomView.findViewById(R.id.editQuantity);

        itemText.setText(name);
        editQuantity.setText(quantity);
        return CustomView;
    };
};
