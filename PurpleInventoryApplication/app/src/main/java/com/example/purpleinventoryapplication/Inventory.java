package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accesses a Cloud Firestore instance from your Activity
 */
public class Inventory {
    /** access to firestore database */
    FirebaseFirestore db;
    /**
     * main activity that we want the toast to show
     */
    Activity activity;
    /**
     * all of the edit text fields
     */
    String ItemName;
    String itemPrice;
    String itemCost;
    String itemQuantity;
    String itemUnit;
    String itemCategory;
    String itemImage;
    HashMap<String, List<String>> categoryInfo;
    List<String> categoryName;
    int selection;
    /**
     * saves response from database
     */
    Boolean success;
    private VolleyOnEventListener<JSONObject> mCallBack;
    public List<Map<String, Object>> itemList;

    Inventory(Activity mActivity) {
        this.db = FirebaseFirestore.getInstance();
        this.success = false;
        this.activity = mActivity;
    }

    /**
     * saves input text to inventory item created
     * @param name
     * @param price
     * @param cost
     * @param quantity
     * @param unit
     * @param category
     */
    public void createItem(String name, String price, String cost, String quantity, String unit, String category) {
        this.ItemName = name;
        this.itemPrice = price;
        this.itemCost = cost;
        this.itemQuantity = quantity;
        this.itemUnit = unit;
        this.itemCategory = category;
    }

    /**
     * creates map for variables of the item
     * writes variables to items collection firestore database
     * notifies user with toast if success or failure
     */
    public void writeData() {
        final String TAG = "Create Item"; // TAG USED FOR LOGGING

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
                                Spinner categoryField = Inventory.this.activity.findViewById(R.id.category);
                                EditText priceField = (EditText) Inventory.this.activity.findViewById(R.id.price);
                                EditText costField = (EditText) Inventory.this.activity.findViewById(R.id.cost);

                                nameField.getText().clear();
                                quantityField.getText().clear();
                                unitField.getText().clear();
                                categoryField.setSelection(0);
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

    /**
     * gets each item from Items collection
     * parameter is a callback function that would allow
     * us to transfer the data taken from the db to another
     * activity.
     * @param mCallback
     */
    public void getAllData(VolleyOnEventListener mCallback) {
        this.mCallBack = mCallback;
        final List<Map<String, Object>> itemList = new ArrayList<>();
        final String TAG = "Get all inventory Items"; // TAG USED FOR LOGGING


        db.collection("items").orderBy("ItemName", Query.Direction.ASCENDING).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map<String, Object> itemMap = document.getData();
                        itemMap.put("ID", document.getId());
                        itemList.add(itemMap);
                    }
                    mCallBack.onSuccess(itemList);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    /**
     * gets Item from Items collection by Id.
     * @param itemId
     */
    public void getDataById(String itemId, final VolleyEventById mCallback) {
        final String TAG = "Display Inventory"; // TAG USED FOR LOGGING
        final Map<String, String> itemMap = new HashMap<>();

        DocumentReference docId = db.collection("items").document(itemId);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());

                        itemMap.put("itemName", document.get("ItemName").toString());
                        itemMap.put("itemPrice", document.get("itemPrice").toString());
                        itemMap.put("itemCost", document.get("itemCost").toString());
                        itemMap.put("itemQuantity", document.get("itemQuantity").toString());
                        itemMap.put("itemUnit", document.get("itemUnit").toString());
                        itemMap.put("itemCategory", document.get("itemCategory").toString());

                        mCallback.onGetDataByIdSuccess(itemMap);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * gets Item from Items collection by Id.
     * @param itemId
     */
    public void getDataName(String itemId) {
        final String TAG = "Display Inventory"; // TAG USED FOR LOGGING
        final String id = itemId;

        DocumentReference docId = db.collection("items").document(id);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                String ItemName = document.get("ItemName").toString();
                                String itemUnit = document.get("itemUnit").toString();

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                Log.d(TAG, "yes clicked.");
                                                deleteDataById(id);
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                };

                                AlertDialog alert = new AlertDialog.Builder(Inventory.this.activity).create();
                                alert.setTitle("Delete Item");
                                alert.setMessage("Are you sure you want to delete "+ItemName+" "+itemUnit+"?");
                                alert.setButton(AlertDialog.BUTTON_POSITIVE, "YES", dialogClickListener);
                                alert.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", dialogClickListener);
                                alert.show();
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

    public void getLowInventory() {
        final String TAG = "LOW Inventory"; // TAG USED FOR LOGGING

        db.collection("items").whereLessThanOrEqualTo("itemQuantity",10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final StringBuilder sb = new StringBuilder();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Log.i(TAG, "DocumentSnapshot data: " + document.getData());

                            String ItemName = document.get("ItemName").toString();
                            String itemUnit = document.get("itemUnit").toString();
                            String itemQuantity = document.get("itemQuantity").toString();

                            sb.append(ItemName+" "+itemUnit+" - "+itemQuantity);
                            sb.append("\n");
                        }
                        else {
                            Log.d(TAG, "No such document");
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {

                            AlertDialog alert = new AlertDialog.Builder(Inventory.this.activity).create();
                            alert.setTitle("You are running low on the following items");
                            alert.setMessage(sb.toString());
                            alert.setButton(AlertDialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    });
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Updates Items collection in Firestore database based on ID
     */
    public void updateDataById(String itemId, Map<String, Object> item, final String source) {
        final String TAG = "Update Data"; // TAG USED FOR LOGGING

        //pasted from firebase docs
        DocumentReference itemRef = db.collection("items").document(itemId);

        itemRef.update(item)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (source == "take") {
                                    Toast.makeText(activity, "Item successfully updated.", Toast.LENGTH_LONG).show();
                                    // Intent intent = new Intent(activity, TakeInventory.class);
                                    // activity.startActivity(intent);
                                } else {
                                    Toast.makeText(activity, "Item successfully updated.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(activity, ViewInventory.class);
                                    activity.startActivity(intent);
                                }
                            }
                        });
                    }
               })
               .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Inventory.this.activity,"Item update failed." ,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
               });
    }

    /**
     * Deletes Items collection in Firestore database based on ID
     * @param itemId
     */
    public void deleteDataById(String itemId) {
        final String TAG = "Delete Item"; // TAG USED FOR LOGGING

        db.collection("items").document(itemId)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Inventory.this.activity,"Item successfully deleted." ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity, ViewInventory.class);
                            activity.startActivity(intent);
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting document", e);
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Inventory.this.activity,"Item deletion failed." ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity, ViewInventory.class);
                            activity.startActivity(intent);
                        }
                    });
                }
            });
    }

    public void getDataByCategory(String category, VolleyOnEventListener mCallback){
        final String TAG = "Get Item by Category"; // TAG USED FOR LOGGING
        this.mCallBack = mCallback;
        final List<Map<String, Object>> itemList = new ArrayList<>();

        db.collection("items")
            .whereEqualTo("itemCategory", category)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Map<String, Object> itemMap = document.getData();
                            itemMap.put("ID", document.getId());
                            itemList.add(itemMap);
                        }
                        mCallBack.onSuccess(itemList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }
}
