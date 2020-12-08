package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
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
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_take_inventory, null);
        }

        final View theView = convertView;

        List<String> item = getDetails(listTitle);
        final String itemId = item.get(0);
        final String itemQty = item.get(1);

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.itemText);
        listTitleTextView.setText(listTitle);

        int editTextId = context.getResources().getIdentifier(String.valueOf(position), "id", context.getPackageName());
        EditText editQty = (EditText) convertView.findViewById(R.id.editIncrement);

        if (editQty == null) {
            editQty = (EditText) convertView.findViewById(editTextId);
        } else {
            editQty.setId(position);
            editQty.setText(itemQty);
        }

        editQty.setTag(itemId);
        Log.d("EDIT TEXT ID", "GETTING ID");
        Log.d("EDIT TEXT ID", String.valueOf(editQty.getId()));

        Button addBtn = (Button) convertView.findViewById(R.id.addInventory);
        addBtn.setTag(position);

        Button subBtn = (Button) convertView.findViewById(R.id.subtractInventory);
        subBtn.setTag(position);

        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // GET THE EDIT TEXT TO UPDATED VALUE
                Object tag = v.getTag();
                Log.d("METHOD CLICK", tag.toString());

                String itemTitle = (String) getGroup((int) tag);
                String itemName = itemTitle.split(" ")[0];
                String itemUnit = itemTitle.split(" ")[1];
                Log.d("METHOD CLICK", itemName);

                int editId = context.getResources().getIdentifier(tag.toString(), "id", context.getPackageName());
                EditText editQty = (EditText) theView.findViewById(editId);
                EditText editQty1 = (EditText) theView.findViewById(R.id.editIncrement);

                int OGQuantity = Integer.parseInt(editQty.getText().toString());
                int newQuantity = OGQuantity + 1;

                Inventory inventory = new Inventory((Activity)TakeInventoryAdapter.this.context);
                Map<String, Object> addQuantity = new HashMap<>();
                addQuantity.put("itemQuantity", newQuantity);
                addQuantity.put("dateUpdated", new Date().getTime());
                inventory.updateDataById(itemId, addQuantity, "take");

                //creates new transaction to record changes if quantity changes.
                Log.d("METHOD CLICK","Creating a transaction");
                Transaction transaction = new Transaction((Activity)TakeInventoryAdapter.this.context);
                transaction.createTransaction(itemName, OGQuantity, newQuantity);

                // SET UPDATED VALUE
                Log.d("METHOD CLICK", String.valueOf(editId));
                editQty.setText(String.valueOf(newQuantity));
            }
        });
        subBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // GET THE EDIT TEXT TO UPDATED VALUE
                Object tag = v.getTag();
                Log.d("METHOD CLICK", tag.toString());

                String itemTitle = (String) getGroup((int) tag);
                String itemName = itemTitle.split(" ")[0];
                String itemUnit = itemTitle.split(" ")[1];
                Log.d("METHOD CLICK", itemName);

                int editId = context.getResources().getIdentifier(tag.toString(), "id", context.getPackageName());
                EditText editQty = (EditText) theView.findViewById(editId);
                EditText editQty1 = (EditText) theView.findViewById(R.id.editIncrement);

                int OGQuantity = Integer.parseInt(editQty.getText().toString());
                int newQuantity = OGQuantity - 1;

                Inventory inventory = new Inventory((Activity)TakeInventoryAdapter.this.context);
                Map<String, Object> addQuantity = new HashMap<>();
                addQuantity.put("itemQuantity", newQuantity);
                addQuantity.put("dateUpdated", new Date().getTime());
                inventory.updateDataById(itemId, addQuantity, "take");

                //creates new transaction to record changes if quantity changes.
                Log.d("METHOD CLICK","Creating a transaction");
                Transaction transaction = new Transaction((Activity)TakeInventoryAdapter.this.context);
                transaction.createTransaction(itemName, OGQuantity, newQuantity);

                // SET THE TEXT TO UPDATED VALUE
                Log.d("METHOD CLICK", String.valueOf(editId));
                editQty.setText(String.valueOf(newQuantity));
            }
        });

        return convertView;
    }
}
