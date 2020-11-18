package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    Activity activity;
    String ItemName;
    String itemPrice;
    String itemCost;
    String itemQuantity;
    String itemUnit;
    String itemCategory;
    String itemImage;
    Boolean success;

    Inventory(Activity mActivity) {
        this.db = FirebaseFirestore.getInstance();
        this.success = false;
        this.activity = mActivity;
    }

    public void createItem(String name, String price, String cost, String quantity, String unit, String category) {
        this.ItemName = name;
        this.itemPrice = price;
        this.itemCost = cost;
        this.itemQuantity = quantity;
        this.itemUnit = unit;
        this.itemCategory = category;
    }

    public void writeData() {
        final String TAG = "Create Item"; // TAG USED FOR LOGGING

        /*
            Create a new item:
            -
        */

        Map<String, Object> item = new HashMap<>();
        item.put("ItemName", this.ItemName);
        item.put("itemPrice", this.itemPrice);
        item.put("itemCost", this.itemCost);
        item.put("itemQuantity", this.itemQuantity);
        item.put("itemUnit", this.itemUnit);
        item.put("itemCategory", this.itemCategory);
        //item.put("itemImage", this.itemImage);
        item.put("dateAdded",new Date().getTime());

        db.collection("items")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Inventory.this.activity, "Successfully Added item", Toast.LENGTH_SHORT).show();
                            Inventory.this.success = true;
                            if(Inventory.this.success) {
                                //add code that pushes item to firebase
                                Log.i(TAG, "Inventory item has been added to firebase");

                                EditText nameField = (EditText) Inventory.this.activity.findViewById(R.id.itemName);
                                EditText quantityField = (EditText) Inventory.this.activity.findViewById(R.id.quantity);
                                EditText unitField = (EditText) Inventory.this.activity.findViewById(R.id.unit);
                                EditText categoryField = (EditText) Inventory.this.activity.findViewById(R.id.category);
                                EditText priceField = (EditText) Inventory.this.activity.findViewById(R.id.price);
                                EditText costField = (EditText) Inventory.this.activity.findViewById(R.id.cost);

                                nameField.getText().clear();
                                quantityField.getText().clear();
                                unitField.getText().clear();
                                categoryField.getText().clear();
                                priceField.getText().clear();
                                costField.getText().clear();

                                Log.i(TAG, "fields have been cleared.");
                            }
                            Inventory.this.success = false;
                        }
                    });
                }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(Inventory.this.activity, "Adding item failed. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getAllData() {

    }

    public void getDataById(String itemId) {
        final String TAG = "Display Inventory"; // TAG USED FOR LOGGING

        /*
            Get specific company using the Id provided on Firestore
        */

        DocumentReference docId = db.collection("items").document(itemId);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Inventory.this.success = true;
                                if(Inventory.this.success) {

                                    Inventory.this.ItemName = document.get("ItemName").toString();
                                    Inventory.this.itemPrice = document.get("itemPrice").toString();
                                    Inventory.this.itemCost = document.get("itemCost").toString();
                                    Inventory.this.itemQuantity = document.get("itemQuantity").toString();
                                    Inventory.this.itemUnit = document.get("itemUnit").toString();
                                    Inventory.this.itemCategory = document.get("itemCategory").toString();

                                    EditText nameField = (EditText) Inventory.this.activity.findViewById(R.id.itemName);
                                    EditText quantityField = (EditText) Inventory.this.activity.findViewById(R.id.quantity);
                                    EditText unitField = (EditText) Inventory.this.activity.findViewById(R.id.unit);
                                    EditText categoryField = (EditText) Inventory.this.activity.findViewById(R.id.category);
                                    EditText priceField = (EditText) Inventory.this.activity.findViewById(R.id.price);
                                    EditText costField = (EditText) Inventory.this.activity.findViewById(R.id.cost);

                                    nameField.setText(Inventory.this.ItemName);
                                    quantityField.setText(Inventory.this.itemPrice);
                                    unitField.setText(Inventory.this.itemCost);
                                    categoryField.setText(Inventory.this.itemQuantity);
                                    priceField.setText(Inventory.this.itemUnit);
                                    costField.setText(Inventory.this.itemCategory);

                                    Log.i(TAG, "fields have been updated.");
                                }
                                Inventory.this.success = false;
                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void updateDataById(Map<String, Object> updates) {

    }

    public void deleteDataById() {

    }
}
