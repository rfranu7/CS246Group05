package com.example.purpleinventoryapplication;

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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Team-05
 *
 */
public class SendReport extends AppCompatActivity {

    private static final String APP_PREFS = "LAST_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("EMAIL", null);
        if(email != null) {
            EditText emailField = (EditText) findViewById(R.id.reportEmail);
            emailField.setText(email);
        }
    }

    public void sendReport(View view) {
        // After button is clicked, save the last email used
        EditText emailField = (EditText) findViewById(R.id.reportEmail);
        final EditText startDateField = (EditText) findViewById(R.id.start_date);
        final EditText endDateField = (EditText) findViewById(R.id.end_date);

        Log.d("SEND REPORT", "REPORT STARTED");

        final String email = emailField.getText().toString();
        Date startDate = new Date(startDateField.getText().toString());
        Date endDate = new Date(endDateField.getText().toString());

        Report report = new Report(this);
        report.getReportByDate(startDate, endDate, new VolleyStringObject() {
            @Override
            public void onGetDataByIdSuccess(Map<String, Object> data) {
                String startDate = startDateField.getText().toString() ;
                String endDate = endDateField.getText().toString();
                export(data, startDate, endDate, email);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.apply();
        


    }

    public void export(Map<String,Object> data,String startDateField, String endDateField, String email){
        //generate data
//        StringBuilder data = new StringBuilder();
//        data.append("Time,Distance");
//        for(int i = 0; i<5; i++){
//            data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
//        }

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = this.getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.purpleinventoryapplication.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            String subject = "Report: " + startDateField + " - " + endDateField;
            String message = "Attached is the report for " + startDateField + " - " + endDateField;

            fileIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email});
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            fileIntent.putExtra(Intent.EXTRA_TEXT, message);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(fileIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}