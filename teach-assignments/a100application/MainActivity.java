package com.example.a100application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onEvenButton (View button) {
        runnableTestEven run = new runnableTestEven(this);

        Thread evens = new Thread(run, "Thread Evens");
        evens.start();

    }

    public void onOddButton (View button) {
        runnableTestOdd run = new runnableTestOdd(this);

        Thread odds = new Thread(run, "Thread Odds");
        odds.start();
    }

}