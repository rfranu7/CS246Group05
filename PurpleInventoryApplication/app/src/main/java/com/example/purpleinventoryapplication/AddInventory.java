package com.example.purpleinventoryapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**Adds inventory item to Firestore collection.
 * @author Team-5
 */
//public class AddInventory extends AppCompatActivity implements LifecycleObserver, View.OnClickListener {
public class AddInventory extends AppCompatActivity {

    private static final String APP_PREFS = "TEMPORARY_FORM_APPLICATION_PREFERENCES";
    private final String TAG = "ADD INVENTORY ACTIVITY";
    private static final int GALLERY_CODE = 1;
    private ImageView imageView;
    private ImageView addPhotoButton;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

//        imageView = findViewById(R.id.imageView5);
//        addPhotoButton = findViewById(R.id.imageButton5);
//        addPhotoButton.setOnClickListener(this);

        addPhotoButton.setVisibility(View.VISIBLE);

    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imageButton5:
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, GALLERY_CODE);
//                break;
//        }
//    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData(); // we have the actual path to the image
                imageView.setImageURI(imageUri);//show image
                addPhotoButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        EditText categoryField = (EditText) findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        String itemName = nameField.getText().toString();
        String quantity = quantityField.getText().toString();
        String unit = unitField.getText().toString();
        String category = categoryField.getText().toString();
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

        Log.d("ON RESUME", sharedPreferences.toString());
        Log.d("ON RESUME", "RESUMED");

        String itemName = sharedPreferences.getString("ITEM_NAME", null);
        String quantity = sharedPreferences.getString("ITEM_QUANTITY", null);
        String unit = sharedPreferences.getString("ITEM_UNIT", null);
        String category = sharedPreferences.getString("ITEM_CATEGORY", null);
        String price = sharedPreferences.getString("ITEM_PRICE", null);
        String cost = sharedPreferences.getString("ITEM_COST", null);

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        EditText categoryField = (EditText) findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        nameField.setText(itemName);
        quantityField.setText(quantity);
        unitField.setText(unit);
        categoryField.setText(category);
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

        Log.d(TAG, "addItem Button clicked.");

        EditText nameField = (EditText) findViewById(R.id.itemName);
        EditText quantityField = (EditText) findViewById(R.id.quantity);
        EditText unitField = (EditText) findViewById(R.id.unit);
        EditText categoryField = (EditText) findViewById(R.id.category);
        EditText priceField = (EditText) findViewById(R.id.price);
        EditText costField = (EditText) findViewById(R.id.cost);

        String itemName = nameField.getText().toString();
        String quantity = quantityField.getText().toString();
        String unit = unitField.getText().toString();
        String category = categoryField.getText().toString();
        String price = priceField.getText().toString();
        String cost = costField.getText().toString();

        Inventory item = new Inventory(this);
        item.createItem(itemName, price, cost, quantity, unit, category);
        item.writeData();
    }

    public void addImage(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}