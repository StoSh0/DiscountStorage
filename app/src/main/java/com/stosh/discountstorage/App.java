package com.stosh.discountstorage;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * @author Ruslan Stosyk
 * Date: May, 27, 2018
 * Time: 23:43
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
