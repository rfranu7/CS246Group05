package com.example.purpleinventoryapplication;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Report {
    /** access to firestore database */
    FirebaseFirestore db;
    /** main activity that we want the toast to show */
    Activity activity;

    Date reportDate;
    double totalCost;
    double totalRevenue;

    Report(Activity mActivity){
        this.db = FirebaseFirestore.getInstance();
        this.activity = mActivity;
        this.reportDate = new Date();
        this.totalCost = 0;
        this.totalRevenue = 0;
    }

    public void getReportByDate(Date reportStartDate, Date reportEndDate, final VolleyStringObject callBack) {
        final String TAG = "GET REPORT"; // TAG USED FOR LOGGING
        Date endDate = new Date(reportEndDate.getYear(), reportEndDate.getMonth(), (reportEndDate.getDate()+1));


        Log.d(TAG, reportStartDate.toString());
        Log.d(TAG, endDate.toString());

        db.collection("transactions")
                .whereGreaterThanOrEqualTo("created", reportStartDate).whereLessThanOrEqualTo("created", endDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Collection used to display each transaction
                            // [name, date, stock/sale, cost, price, revenue]
                            ArrayList<ArrayList<Object>> stockTransactions = new ArrayList<>();
                            ArrayList<ArrayList<Object>> saleTransactions = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> itemMap = document.getData();
                                ArrayList<Object> stockItemTransactions = parseStockTransaction(itemMap);
                                ArrayList<Object> saleItemTransactions = parseSaleTransaction(itemMap);

                                // total spend calculated from stock
                                Report.this.totalCost += Double.parseDouble(stockItemTransactions.get(3).toString());

                                // total sale calculated from stock
                                Report.this.totalRevenue += Double.parseDouble(saleItemTransactions.get(5).toString());

                                stockTransactions.add(stockItemTransactions);
                                saleTransactions.add(saleItemTransactions);
                            }

                            Log.d(TAG, String.valueOf(Report.this.totalCost));
                            Log.d(TAG, String.valueOf(Report.this.totalRevenue));
                            display(stockTransactions, saleTransactions);


                            Map<String, Object> reportFields = new HashMap<>();
                            reportFields.put("stockTransactions", stockTransactions);
                            reportFields.put("saleTransactions", saleTransactions);
                            reportFields.put("totalCost", Report.this.totalCost);
                            reportFields.put("totalRevenue", Report.this.totalRevenue);
                            callBack.onGetDataByIdSuccess(reportFields);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void display(ArrayList<ArrayList<Object>> stock, ArrayList<ArrayList<Object>> sale){
        String stockTag = "STOCKS"; // TAG USED FOR LOGGING
        String saleTag = "SALES"; // TAG USED FOR LOGGING

        Log.d(stockTag, "*************************");
        Log.d(stockTag, "[NAME, DATE, STOCK, COST, PRICE, POTENTIAL REVENUE]");
        for(int i=0; i<stock.size(); i++){
            Log.d(stockTag, stock.get(i).toString());
        }
        Log.d(stockTag, "*************************");

        Log.d(saleTag, "*************************");
        Log.d(saleTag, "[NAME, DATE, SALE, COST, PRICE, REVENUE]");
        for(int i=0; i<sale.size(); i++){
            Log.d(saleTag, sale.get(i).toString());
        }
        Log.d(saleTag, "*************************");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Object> parseStockTransaction(Map<String, Object> transaction){
        String name = transaction.get("itemName").toString();

        Timestamp timestamp = (Timestamp) transaction.get("created");
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000;
        ZoneId tz = ZoneId.systemDefault();
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), tz);

        int stock = 0;
        double cost = 0;
        double price = 0;

        ArrayList<Map<String, Object>> transactionList = (ArrayList<Map<String, Object>>) transaction.get("transaction");
        for(int i=0; i < transactionList.size(); i++) {
            Log.d("TYPE", transactionList.get(i).get("type").toString());
            if(transactionList.get(i).get("type").toString().equals("stock")){
                stock += Integer.parseInt(transactionList.get(i).get("difference").toString());
                cost += Double.parseDouble(transactionList.get(i).get("totalCost").toString());
                price += Double.parseDouble(transactionList.get(i).get("totalPrice").toString());
            } else {
                continue;
            }
        }
        double potentialRevenue = (price-cost)*stock;

        ArrayList<Object> transactionTotal = new ArrayList<>();
        transactionTotal.add(name);
        transactionTotal.add(date);
        transactionTotal.add(stock);
        transactionTotal.add(cost);
        transactionTotal.add(price);
        transactionTotal.add(potentialRevenue);

        return transactionTotal;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Object> parseSaleTransaction(Map<String, Object> transaction){
        String name = transaction.get("itemName").toString();

        Timestamp timestamp = (Timestamp) transaction.get("created");
        long milliseconds = timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000;
        ZoneId tz = ZoneId.systemDefault();
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), tz);

        int sale = 0;
        double cost = 0;
        double price = 0;

        ArrayList<Map<String, Object>> transactionList = (ArrayList<Map<String, Object>>) transaction.get("transaction");
        for(int i=0; i < transactionList.size(); i++) {
            if(transactionList.get(i).get("type").toString().equals("sale")){
                sale += Integer.parseInt(transactionList.get(i).get("difference").toString());
                cost += Double.parseDouble(transactionList.get(i).get("totalCost").toString());
                price += Double.parseDouble(transactionList.get(i).get("totalPrice").toString());
            } else {
                continue;
            }
        }
        double revenue = (price-cost)*sale;

        ArrayList<Object> transactionTotal = new ArrayList<>();
        transactionTotal.add(name);
        transactionTotal.add(date);
        transactionTotal.add(sale);
        transactionTotal.add(cost);
        transactionTotal.add(price);
        transactionTotal.add(revenue);

        return transactionTotal;
    }
}
