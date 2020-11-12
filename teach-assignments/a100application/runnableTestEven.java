package com.example.a100application;

import android.app.Activity;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class runnableTestEven implements Runnable {
    WeakReference<Activity> activityRef;

    public runnableTestEven(Activity activityPass) {
        activityRef = new WeakReference<Activity>(activityPass);
    }
    @Override
    public void run() {
        for (int i = 0; i < 101; i += 2) {
            System.out.println(i);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (activityRef != null) {
            final Activity activity = activityRef.get();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(activity, "Finished counting Evens", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

}
