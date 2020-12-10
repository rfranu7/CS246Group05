package com.example.purpleinventoryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Randeep
 * uses Firebase prebuilt authentication to reset password
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "FP ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart() {
        super.onStart();
        // redirect to dashboard page
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void resetPassword(View view) {
        TextView emailAddressField = (TextView) findViewById(R.id.forgotEmailAddress);
        String emailAddress = emailAddressField.getText().toString();

        if(emailAddress.isEmpty()) {
            Toast.makeText(this, "Email Address cannot be blank",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(ForgotPasswordActivity.this, "Please Check your email",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * returns user to login activity
     * @param view
     */
    public void loginLink(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}