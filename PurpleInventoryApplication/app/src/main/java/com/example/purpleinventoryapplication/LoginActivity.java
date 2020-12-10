package com.example.purpleinventoryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.purpleinventoryapplication.ForgotPasswordActivity;
import com.example.purpleinventoryapplication.MainActivity;
import com.example.purpleinventoryapplication.RegisterActivity;
import com.example.purpleinventoryapplication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 *  @author Randeep
 *  uses Firebase prebuilt authentication to log in user
 *
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final String TAG = "LOGIN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            // redirect user to dashboard
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * gets username and password from input text
     * checks login info against data in firestore authentication
     * @param view
     */
    public void LoginUser(View view) {
        TextView emailAddressField = (TextView) findViewById(R.id.loginEmailAddress);
        String emailAddress = emailAddressField.getText().toString();

        TextView passwordField =  (TextView) findViewById(R.id.loginPassword);
        String password = passwordField.getText().toString();

        if(emailAddress.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email Address or password cannot be blank",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i(TAG, "signInWithEmail:success");

                        // Get Instance of user and save it in shared preferences
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String uid = currentUser.getUid();
                        User newUser = new User();
                        newUser.setUserId(uid, LoginActivity.this);
                        newUser.getDataById();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /**
     * creates intent for RegisterActivity
     * @param view
     */
    public void registerUserLink(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    /**
     * creates intent for ForgotPasswordActivity
     * @param view
     */
    public void forgotPasswordLink(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}