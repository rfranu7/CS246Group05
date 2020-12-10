package com.example.purpleinventoryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author randeep
 * uses firebase authentication to create a new user
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "REGISTER ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            // redirect user to dashboard page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Gets email, password, and name from input text
     * puts login data into firebase authentication
     * @param view
     */
    public void registerUser(View view) {
        TextView emailAddressField = (TextView) findViewById(R.id.registerEmailAddress);
        final String emailAddress = emailAddressField.getText().toString();

        TextView passwordField =  (TextView) findViewById(R.id.registerPassword);
        final String password = passwordField.getText().toString();

        TextView nameField =  (TextView) findViewById(R.id.registerName);
        final String name = nameField.getText().toString();

        final String company = "Purple Store";

        if(emailAddress.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Email Address, password or name cannot be blank",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String uid = currentUser.getUid();

                            // Write credentials in Fire Store
                            User newUser = new User();
                            newUser.createUser(uid, emailAddress, name, company);
                            newUser.writeData();

                            // Save the details on shared preferences for future calls
                            SharedPreferences sharedPreferences = getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", uid);
                            editor.putString("emailAddress", emailAddress);
                            editor.putString("name", name);
                            editor.putString("companyId", company);
                            editor.apply();

                            // Redirect to Dashboard
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * creates intent for loginActivity
     * @param view
     */
    public void loginLink(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}