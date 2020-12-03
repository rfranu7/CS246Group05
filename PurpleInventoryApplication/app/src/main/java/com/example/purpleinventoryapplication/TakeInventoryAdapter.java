package com.example.purpleinventoryapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeInventoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> listTitle;
    private HashMap<String, List<String>> listDetail;

    public TakeInventoryAdapter(Context context, List<String> listTitle, HashMap<String, List<String>> listDetail) {
        this.context = context;
        this.listTitle = listTitle;
        this.listDetail = listDetail;
    }


    @Override
    public int getCount() {
        return listTitle.size();
    }

    @Override
    public Object getItem(int position) {
        return listDetail.get(position);
    }

    public List<String> getDetails(String key) {
        return listDetail.get(key);
    }

    public Object getGroup(int listPosition) {
        return this.listTitle.get(listPosition);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_take_inventory, null);
        }

        List<String> item = getDetails(listTitle);
        String itemId = item.get(0);
        String itemQty = item.get(1);

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.itemText);
        listTitleTextView.setText(listTitle);

        EditText editQty = (EditText) convertView.findViewById(R.id.editQuantity);
        editQty.setText(itemQty);

        Button addBtn = (Button) convertView.findViewById(R.id.addInventory);
        addBtn.setTag(itemId);

        Button subBtn = (Button) convertView.findViewById(R.id.subtractInventory);
        subBtn.setTag(itemId);


        // TODO replace findViewById by ViewHolder
//        ((TextView) convertView.findViewById(R.id.itemText)).setText(item.getKey());
//        ((EditText) convertView.findViewById(R.id.editQuantity)).setText(data.get(1));

        return convertView;
    }
}
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class TakeInventoryAdapter extends ArrayAdapter<Map<String, List<String>>> {
//    private final String TAG = "TAKE INVENTORY ADAPTER";
//    public String name;
//    public String [] itemData;
//    public String ID;
//    public String quantity;
//
//
//    public TakeInventoryAdapter(@NonNull VolleyOnEventListener context, Map<String, List<String>> Items) {
//        super((Context) context, R.layout.activity_take_inventory, (List<Map<String, List<String>>>) Items);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        LayoutInflater myInflater = LayoutInflater.from(getContext());
//        View CustomView = myInflater.inflate(R.layout.activity_take_inventory, parent, false);
//
//        Map<String, List<String>> singleItem = getItem(position);
//
//        for (String key : singleItem.keySet()) {
//            name = key;
//            List data = singleItem.get(key);
//            ID = (String) data.get(0);
//            quantity = (String) data.get(1);
//        }
//
//        TextView itemText = (TextView) CustomView.findViewById(R.id.itemText);
//        EditText editQuantity = (EditText) CustomView.findViewById(R.id.editQuantity);
//
//        itemText.setText(name);
//        editQuantity.setText(quantity);
//        return CustomView;
//    };
//};
