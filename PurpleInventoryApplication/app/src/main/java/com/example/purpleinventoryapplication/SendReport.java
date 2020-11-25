package com.example.purpleinventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        String email = emailField.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.apply();
    }
}