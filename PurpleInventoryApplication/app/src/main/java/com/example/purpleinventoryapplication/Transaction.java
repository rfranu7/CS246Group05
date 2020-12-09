package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Transaction {
    /**access to firestore database*/
    FirebaseFirestore db;
    /**
     * main activity that we want the toast to show
     */
    Activity activity;
    /**
     * all of the document fields
     */

    String transactionId;
    String transactionName;
    String itemName;
    int originalQuantity;
    int finalQuantity;
    double cost;
    double price;
    boolean success;

    private VolleyOnEventListener<JSONObject> mCallBack;

    //todo call from take inventory and edit inventory

    Transaction(Activity mActivity) {
        this.db = FirebaseFirestore.getInstance();
        this.activity = mActivity;

    }

    /**
     * creates a unique string with item name and date .
     * will be used to check to see if there is already a document for that day.
     * will be the transaction ID in the firestore transaction document
     * @param itemName
     * @return transactionId
     */
    public String setTransactionId(String itemName, String itemUnit) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String transactionId = itemName +"-"+itemUnit
                +"-"+ (calendar.get(Calendar.MONTH)+1)
                +"-"+ calendar.get(Calendar.DAY_OF_MONTH)
                +"-"+ calendar.get(Calendar.YEAR);
        return transactionId;
    }

    /**
     * Public setter for Transaction Class
     * @param itemName
     * @param originalQuantity
     * @param finalQuantity
     */
    public void setProperties(String itemName, int originalQuantity, int finalQuantity, double cost, double price){
        this.itemName = itemName;
        this.originalQuantity = originalQuantity;
        this.finalQuantity = finalQuantity;
        this.cost = cost;
        this.price = price;
    }

    //todo
    public void getDataById(String transactionId) {
        final String TAG = "Display Inventory"; // TAG USED FOR LOGGING

        DocumentReference docId = db.collection("transactions").document(transactionId);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Transaction.this.success = true;
                                if(Transaction.this.success) {

                                    Transaction.this.transactionId = document.get("TransactionID").toString();
                                    Transaction.this.transactionName = document.get("transactionName").toString();
                                    Transaction.this.itemName = document.get("itemCost").toString();
                                    Transaction.this.originalQuantity = (int) document.get("originalQuantity");
                                    Transaction.this.finalQuantity = (int) document.get("itemUnit");

                                }
                                Transaction.this.success = false;
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

    /**
     * Creates or updates transaction
     * @param itemName
     * @param originalQuantity
     * @param finalQuantity
     */
    public void createTransaction(final String itemName, final String itemUnit, int originalQuantity, int finalQuantity, double cost, double price) {
        final String TAG = "Create Transaction"; // TAG USED FOR LOGGING
        final String transID = setTransactionId(itemName, itemUnit);
        setProperties(itemName, originalQuantity, finalQuantity, cost, price);

        final DocumentReference docId = db.collection("transactions").document(transID);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());
                        ArrayList<Map<String, Object>> prevTransaction = (ArrayList<Map<String, Object>>) document.get("transaction");
                        Map<String, Object> transactionItems = new HashMap<>();
                        int difference = 0;

                        // Identify the type based on the number of final quantity
                        if(Transaction.this.originalQuantity == Transaction.this.finalQuantity) {
                            return; // No change in quantity means no transaction made.
                        } else if(Transaction.this.originalQuantity < Transaction.this.finalQuantity) {
                            transactionItems.put("type", "stock");
                            difference = Transaction.this.finalQuantity - Transaction.this.originalQuantity;
                        } else {
                            transactionItems.put("type", "sale");
                            difference = Transaction.this.originalQuantity - Transaction.this.finalQuantity;
                        }
                        transactionItems.put("date", new Date());
                        transactionItems.put("difference", difference);

                        double totalCost = Transaction.this.cost*difference;
                        transactionItems.put("totalCost", totalCost);

                        double totalPrice = Transaction.this.price*difference;
                        transactionItems.put("totalPrice", totalPrice);

                        prevTransaction.add(transactionItems);

                        Map<String, Object> transaction = new HashMap<>();
                        transaction.put("finalQuantity", Transaction.this.finalQuantity);
                        transaction.put("transaction", prevTransaction);

                        Log.d(TAG, "calling update");
                        updateDataById(transID, transaction);
                    } else {
                        Log.d(TAG, "No such document");
                        ArrayList<Map<String, Object>> transactionRecords = new ArrayList<>();
                        Map<String, Object> transactionItems = new HashMap<>();
                        int difference = 0;

                        Log.d(TAG, String.valueOf(Transaction.this.originalQuantity));
                        Log.d(TAG, String.valueOf(Transaction.this.finalQuantity));

                        // Identify the type based on the number of final quantity
                        if(Transaction.this.originalQuantity == Transaction.this.finalQuantity) {
                            return; // No change in quantity means no transaction made.
                        } else if(Transaction.this.originalQuantity < Transaction.this.finalQuantity) {
                            transactionItems.put("type", "stock");
                            difference = Transaction.this.finalQuantity - Transaction.this.originalQuantity;
                        } else {
                            transactionItems.put("type", "sale");
                            difference = Transaction.this.originalQuantity - Transaction.this.finalQuantity;
                        }
                        transactionItems.put("date", new Date());
                        transactionItems.put("difference", difference);
                        transactionItems.put("finalQuantity", Transaction.this.finalQuantity);

                        double totalCost = Transaction.this.cost*difference;
                        transactionItems.put("totalCost", totalCost);

                        double totalPrice = Transaction.this.price*difference;
                        transactionItems.put("totalPrice", totalPrice);

                        transactionRecords.add(transactionItems);

                        Map<String, Object> transaction = new HashMap<>();
                        transaction.put("itemName", Transaction.this.itemName);
                        transaction.put("originalQuantity", Transaction.this.originalQuantity);
                        transaction.put("finalQuantity", Transaction.this.finalQuantity);
                        transaction.put("created",new Date());
                        transaction.put("transaction", transactionRecords);

                        Log.d(TAG, "calling write");
                        writeData(transID, transaction);
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Writes/Creates the transaction to Fire Store
     * @param transactionId
     */
    public void writeData(String transactionId, Map<String, Object> transaction) {
        final String TAG = "Create Transaction"; // TAG USED FOR LOGGING

        db.collection("transactions")
                .document(transactionId)
                .set(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * Updates the transaction collection in Fire Store database based on ID
     * @param transactionId
     * @param transaction
     */
    public void updateDataById(String transactionId, Map<String, Object> transaction) {
        final String TAG = "Update Data"; // TAG USED FOR LOGGING

        //pasted from firebase docs
        DocumentReference TransactionRef = db.collection("transactions").document(transactionId);

        TransactionRef.update(transaction)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                }
            });
    }

}
