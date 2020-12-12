package com.example.purpleinventoryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Team-05
 *
 */
public class SendReport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);
    }

    public void uploadReport(View view) {
        // After button is clicked, save the last email used
        final EditText startDateField = (EditText) findViewById(R.id.start_date);
        final EditText endDateField = (EditText) findViewById(R.id.end_date);

        Log.d("UPLOAD REPORT", "REPORT STARTED");

        Date startDate = new Date(startDateField.getText().toString());
        Date endDate = new Date(endDateField.getText().toString());

        Report report = new Report(this);
        report.getReportByDate(startDate, endDate, new VolleyStringObject() {
            @Override
            public void onGetDataByIdSuccess(Map<String, Object> data) {
                String startDate = startDateField.getText().toString() ;
                String endDate = endDateField.getText().toString();
                export(data, startDate, endDate);
            }
        });
    }

    public void export(Map<String,Object> data,String startDateField, String endDateField){
        final String TAG = "UPLOAD FILE";

        ArrayList<ArrayList<Object>> stockTransactions = (ArrayList<ArrayList<Object>>) data.get("stockTransactions");
        ArrayList<ArrayList<Object>> saleTransactions = (ArrayList<ArrayList<Object>>) data.get("saleTransactions");
        double totalCost = (double) data.get("totalCost");
        double totalRevenue = (double) data.get("totalRevenue");

        //generate data
        StringBuilder formattedDate = new StringBuilder();


        formattedDate.append("Report,"+startDateField+"-"+endDateField+"\n\n");

        formattedDate.append("total Cost,"+totalCost+"\n");
        formattedDate.append("total Revenue,"+totalRevenue+"\n\n");

        formattedDate.append("Name,Date,Stock,Cost,Price,Potential Revenue\n");
        for(int i = 0; i<stockTransactions.size(); i++) {
            for(int j = 0; j<6; j++){
                if(j==5){
                    formattedDate.append(stockTransactions.get(i).get(j));
                } else {
                    formattedDate.append(stockTransactions.get(i).get(j) + ",");
                }
            }
            formattedDate.append("\n");
        }

        formattedDate.append("\n\n");

        formattedDate.append("Name,Date,Stock,Cost,Price,Revenue\n");
        for(int i = 0; i<saleTransactions.size(); i++) {
            for(int j = 0; j<6; j++){
                if(j==5){
                    formattedDate.append(saleTransactions.get(i).get(j));
                } else {
                    formattedDate.append(saleTransactions.get(i).get(j) + ",");
                }
            }
            formattedDate.append("\n");
        }

        Log.d(TAG, formattedDate.toString());

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((formattedDate.toString()).getBytes());
            out.close();

            //exporting
            Context context = this.getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.purpleinventoryapplication.fileprovider", filelocation);
            Log.d(TAG, filelocation.toString());
            Log.d(TAG, path.toString());

            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);

            String title = "Report: " + startDateField + " - " + endDateField+".csv";
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, title);

            startActivity(Intent.createChooser(fileIntent, "Upload Report"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}