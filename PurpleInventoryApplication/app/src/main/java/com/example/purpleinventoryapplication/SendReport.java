package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

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
        EditText startDateField = (EditText) findViewById(R.id.start_date);
        EditText endDateField = (EditText) findViewById(R.id.end_date);

        Log.d("SEND REPORT", "REPORT STARTED");

        String email = emailField.getText().toString();
        Date startDate = new Date(startDateField.getText().toString());
        Date endDate = new Date(endDateField.getText().toString());

        Report report = new Report(this);
        report.getReportByDate(startDate, endDate);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.apply();
    }
}