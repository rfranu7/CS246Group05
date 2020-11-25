package com.example.purpleinventoryapplication;

import java.util.Map;

/**
 * @author Randeep
 * <p>
 *  Use this link as reference for the Firestore documentation
 *         Add and Update Data - https://firebase.google.com/docs/firestore/manage-data/add-data#update-data
 *         Get (read) All and Get by Id - https://firebase.google.com/docs/firestore/query-data/get-data
 *         Delete Data - https://firebase.google.com/docs/firestore/manage-data/delete-data
 */
public interface FirestoreAccess {

    /*
        Use this link as reference for the Firestore documentation
        Add and Update Data - https://firebase.google.com/docs/firestore/manage-data/add-data#update-data
        Get (read) All and Get by Id - https://firebase.google.com/docs/firestore/query-data/get-data
        Delete Data - https://firebase.google.com/docs/firestore/manage-data/delete-data
     */

    public void writeData();
    public void getAllData();
    public void getDataById();
    public void updateDataById(Map<String, Object> updates);
    public void deleteDataById();

}
