package com.example.mr.khan2;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by MR on 12/3/2017.
 */

public class Whatapps extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
