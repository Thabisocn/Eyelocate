package com.example.user.eyelocate;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class offline extends Application {


    public void onCreate() {


        super.onCreate();


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

}
