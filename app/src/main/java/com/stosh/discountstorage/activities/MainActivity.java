package com.stosh.discountstorage.activities;


import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.AuthFragmentListener;
import com.stosh.discountstorage.fragments.auth.LoginFragment;
import com.stosh.discountstorage.fragments.auth.PasswordResetFragment;
import com.stosh.discountstorage.fragments.auth.SingUpFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements AuthFragmentListener,
        FirebaseAuth.AuthStateListener {

    private String TAG = "MainActivity";
    private Unbinder unbinder;
    private FireBaseSingleton fireBase;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fireBase = FireBaseSingleton.getInstance();
        unbinder = ButterKnife.bind(this);
        fireBase.check(this);
    }

    @Override
    public void clickBtn(int code) {
        switch (code) {
            case Const.LOGIN_ID:
                startLoginFragment();
                break;
            case Const.SING_UP_ID:
                startSingUpFragment();
                break;
            case Const.RESET_ID:
                startResetFragment();
                break;
        }
    }

    private void startLoginFragment() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_fade_in,
                        R.anim.fragment_fade_out)
                .replace(R.id.containerLogin, LoginFragment.getInstance(null))
                .commit();
    }

    private void startSingUpFragment() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_fade_in,
                        R.anim.fragment_fade_out)
                .replace(R.id.containerLogin, SingUpFragment.getInstance(null))
                .commit();
    }

    private void startResetFragment() {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_fade_in,
                        R.anim.fragment_fade_out,
                        R.anim.fragment_fade_pop_in,
                        R.anim.fragment_fade_pop_out)
                .replace(R.id.containerLogin, PasswordResetFragment.getInstance(null))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            startActivity(new Intent(MainActivity.this, DrawerActivity.class));
            finish();
        } else {
            Log.d(TAG, "onAuthStateChanged:signed_out");
            mFragmentManager = getSupportFragmentManager();
            startLoginFragment();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        fireBase.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        fireBase.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
