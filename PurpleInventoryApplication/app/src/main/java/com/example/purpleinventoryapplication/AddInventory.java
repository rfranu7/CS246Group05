package com.example.purpleinventoryapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Adds inventory item to Firestore collection.
 * @author Team-5
 */
public class AddInventory extends AppCompatActivity implements LifecycleObserver {

    private static final String APP_PREFS = "TEMPORARY_FORM_APPLICATION_PREFERENCES";
    private final String TAG = "ADD INVENTORY ACTIVITY";
    private static final int GALLERY_CODE = 1;
    HashMap<String, List<String>> categoryInfo;
    List<String> categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);


        final Spinner dropdown = findViewById(R.id.category);

        Category categories = new Category(this);

        categories.getAllData(new VolleyOnEventListener() {
            @Override
            public void onSuccess(List response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, "Getting data successful");

                List<Map<String, Object>> data = response;
                categoryInfo = new HashMap<String, List<String>>();

                for(int i=0; i < data.size(); i++) {
                    String name = data.get(i).get("categoryName").toString();
                    String created = data.get(i).get("created").toString();

                    List<String> detail = new ArrayList<String>();
                    detail.add(name);
                    detail.add(created);
                    categoryInfo.put(name, detail);
                }

                categoryName = new ArrayList<>(categoryInfo.keySet());
                Log.d(TAG, categoryName.toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddInventory.this, android.R.layout.simple_spinner_dropdown_item, categoryName);

                dropdown.setAdapter(adapter);
            }

        });

    }


    @Override
    protected void onPause() {
        super.onPause();

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        Spinner categoryField = findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        String itemName = nameField.getText().toString();
        String quantity = quantityField.getText().toString();
        String unit = unitField.getText().toString();
        String category = categoryField.getSelectedItem().toString();
        String price = priceField.getText().toString();
        String cost = costField.getText().toString();

        SharedPreferences sharedPref = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ITEM_NAME", itemName);
        editor.putString("ITEM_QUANTITY", quantity);
        editor.putString("ITEM_UNIT", unit);
        editor.putString("ITEM_CATEGORY", category);
        editor.putString("ITEM_PRICE", price);
        editor.putString("ITEM_COST", cost);
        editor.apply();


        Log.d("ON PAUSE", sharedPref.toString());
        Log.d("ON PAUSE", "PAUSED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);

//        Log.d("ON RESUME", sharedPreferences.toString());
//        Log.d("ON RESUME", "RESUMED");

        String itemName = sharedPreferences.getString("ITEM_NAME", null);
        String quantity = sharedPreferences.getString("ITEM_QUANTITY", null);
        String unit = sharedPreferences.getString("ITEM_UNIT", null);
        String category = sharedPreferences.getString("ITEM_CATEGORY", null);
        String price = sharedPreferences.getString("ITEM_PRICE", null);
        String cost = sharedPreferences.getString("ITEM_COST", null);

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        Spinner categoryField = findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        nameField.setText(itemName);
        quantityField.setText(quantity);
        unitField.setText(unit);
        priceField.setText(price);
        costField.setText(cost);
    }

    /**
     * adds inventory item to items collection in Firestore.
     * <p>
     *     collects inputed textfield
     *     converts input to strings
     *     creates instance of inventory class
     *     calls {@link Inventory#createItem(String name, String price, String cost, String quantity, String unit, String category)}
     *     calls {@link Inventory#writeData()}
     *
     * @param view
     */
    public void addItem(View view) {

//        Log.d(TAG, "addItem Button clicked.");

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        Spinner categoryField = findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        String itemName = nameField.getText().toString();
        String quantity = quantityField.getText().toString();
        String unit = unitField.getText().toString();
        String category = categoryField.getSelectedItem().toString();
        String price = priceField.getText().toString();
        String cost = costField.getText().toString();

        Inventory item = new Inventory(this);
        item.createItem(itemName, price, cost, quantity, unit, category);
        item.writeData();

        //creates new transaction to record changes.
//        Log.d(TAG,"Creating a transaction");
        Transaction transaction = new Transaction(this);
        transaction.createTransaction(itemName, unit, 0, Integer.parseInt(quantity), Double.parseDouble(cost), Double.parseDouble(price) );
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}