package com.stosh.discountstorage.refactoring.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stosh.discountstorage.R;

/**
 * @author Ruslan Stosyk
 *         Date: November, 24, 2017
 *         Time: 4:31 PM
 */
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}
