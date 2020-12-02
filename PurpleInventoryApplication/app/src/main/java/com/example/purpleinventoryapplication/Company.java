package com.example.purpleinventoryapplication;

import android.util.Log;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Team-05
 *
 */
public class Company implements FirestoreAccess{
    // Access a Cloud Firestore instance from your Activity
    /**
     * access to firestore database
     */
    FirebaseFirestore db;
    /**
     * company ID to access firebase
     */
    String companyId;
    /**
     * Business name linked to company ID
     */
    String businessName;

    Company() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * creates a company
     * @param businessName
     */
    public void CompanyName(String businessName) {
        this.companyId = businessName;
        this.businessName = businessName;
    }

    /**
     * @author Team-05
     *
     * <p>
     *   Create a new company:
     *   name and id will be the same so we can search
     *   for the business id using the name of the business itself.
     *   this would make it easier to attach other classes to the
     *   company on the database.
     *
     */
    @Override
    public void writeData() {
        final String TAG = "Create Company"; // TAG USED FOR LOGGING

        Map<String, Object> company = new HashMap<>();
        company.put("companyId", this.companyId);
        company.put("businessName", this.businessName);
        company.put("created",new Date().getTime());

        db.collection("companies")
            .document(this.companyId)
            .set(company)
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
     * @author Team-05
     *
     *  Get All companies on Firestore
     */
    @Override
    public void getAllData() {
        final String TAG = "Display Companies"; // TAG USED FOR LOGGING

        db.collection("companies")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
    }

    /**
     * gets company based on companyID
     */
    @Override
    public void getDataById() {
        final String TAG = "Display Company"; // TAG USED FOR LOGGING

        DocumentReference docId = db.collection("companies").document(this.companyId);
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

    /**
     * @author Team-05
     * updates data in firestore collection by ID
     * @param updates
     *
     */
    @Override
    public void updateDataById(Map<String, Object> updates) {
        final String TAG = "Update Company"; // TAG USED FOR LOGGING

        DocumentReference docId = db.collection("companies").document(this.companyId);
        docId.update(updates)
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

    /**
     * Deletes data from firestore based on ID
     */
    @Override
    public void deleteDataById() {
        final String TAG = "Delete Company"; // TAG USED FOR LOGGING

        db.collection("companies").document(this.companyId)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error deleting document", e);
                }
            });
    }
}
