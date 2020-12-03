package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {
    /** access to firestore database */
    FirebaseFirestore db;
    /**
     * main activity that we want the toast to show
     */
    Activity activity;
    /**
     * all of the edit text fields
     */
    String categoryId;
    String categoryName;

    private VolleyOnEventListener<JSONObject> mCallBack;

    Category(Activity mActivity) {
        this.db = FirebaseFirestore.getInstance();
        this.activity = mActivity;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        this.categoryName = categoryId;
    }

    public void writeData() {
        final String TAG = "Create Company"; // TAG USED FOR LOGGING

        Map<String, Object> category = new HashMap<>();
        category.put("categoryName", this.categoryName);
        category.put("created",new Date().getTime());

        db.collection("categories")
            .document(this.categoryId)
            .set(category)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Category.this.activity,"Item successfully added." ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity, CategoryList.class);
                            activity.startActivity(intent);
                        }
                    });
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
     * gets each item from Items collection
     * parameter is a callback function that would allow
     * us to transfer the data taken from the db to another
     * activity.
     * @param mCallback
     */
    public void getAllData(VolleyOnEventListener mCallback) {
        this.mCallBack = mCallback;
        final List<Map<String, Object>> categoryList = new ArrayList<>();
        final String TAG = "Get all inventory Items"; // TAG USED FOR LOGGING

        db.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Map<String, Object> categoryMap = document.getData();
                        categoryList.add(categoryMap);
                    }
                    mCallBack.onSuccess(categoryList);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    /**
     * gets company based on companyID
     */
    public void getDataById() {
        final String TAG = "Display Company"; // TAG USED FOR LOGGING

        DocumentReference docId = db.collection("companies").document(this.categoryId);
        docId.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
