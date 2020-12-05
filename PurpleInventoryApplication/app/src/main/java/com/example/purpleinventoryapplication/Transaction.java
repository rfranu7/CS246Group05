package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.List;
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
    boolean success;

    private VolleyOnEventListener<JSONObject> mCallBack;

    //todo call from take inventory and edit inventory

    Transaction(Activity mActivity) {
        this.db = FirebaseFirestore.getInstance();
        this.activity = mActivity;

    }

    /**
     *
     * @param itemName
     * @return transactionId
     *
     * creates a unique string with item name and date .
     * will be used to check to see if there is already a document for that day.
     * will be the transaction ID in the firestore transaction document
     */
    public String setTransactionId(String itemName) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String transactionId = itemName + calendar.get(Calendar.MONTH)
                + calendar.get(Calendar.DAY_OF_MONTH)
                + calendar.get(Calendar.YEAR);
        return transactionId;

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

//    public void checkTransactionId(String transactionId) {
//        final String TAG = "Display Inventory"; // TAG USED FOR LOGGING
//        DocumentReference docId = db.collection("items").document(transactionId);
//        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    final DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());
//                        Transaction.this.success = true;
//                    } else {
//                        Log.i(TAG, "DocumentSnapshot data: " + document.getData());
//                    }
//                        Transaction.this.success = false;
//
//                }else {
//                    Log.d(TAG, "No such document");
//            }
//
//    });
//    }
//


    public void createTransaction(String transactionId, String itemName, int originalQuantity, int finalQuantity) {
//       String transID = getDataById(transactionId);
//    todo create transaction-> if success = true {update data by id} else write data.

        this.itemName = itemName;
        this.originalQuantity = originalQuantity;
        this.finalQuantity = finalQuantity;

    }
    //todo get original quantity and final quantity from take inventory or edit inventory.

//    public void createOrUpdateTransaction(String transactionId) {
//        getDataById(transactionId);
//    }







    public void writeData() {
        final String TAG = "Create Transaction"; // TAG USED FOR LOGGING

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("transactionName", this.transactionName);
        transaction.put("created",new Date().getTime());
        transaction.put("itemName", this.itemName);
        transaction.put("originalQuantity", this.originalQuantity);
        transaction.put("finalQuantity", this.finalQuantity);


        db.collection("categories")
                .document(this.transactionId)
                .set(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                        Toast.makeText(Transaction.this.activity,"Transaction history updated")
//                        activity.runOnUiThread(new Runnable() {
//                            public void run() {
//                                Toast.makeText(Category.this.activity,"Item successfully added." ,Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(activity, CategoryList.class);
//                                activity.startActivity(intent);
//                            }
//                        })
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

}
