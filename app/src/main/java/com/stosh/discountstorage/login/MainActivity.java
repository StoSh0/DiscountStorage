package com.stosh.discountstorage.login;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.drawer.DrawerActivity;
import com.stosh.discountstorage.login.fragments.LoginFragment;
import com.stosh.discountstorage.login.fragments.PasswordResetFragment;
import com.stosh.discountstorage.login.fragments.SingUpFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements LoginFragment.ListenerFragment,
        SingUpFragment.ListenerFragment {

    private final int LOGIN_ID = 1;
    private final int SING_UP_ID = 2;
    private final int RESET_ID = 3;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "MainActivity";
    private Unbinder unbinder;
    private FireBaseSingleton fireBase;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fireBase = FireBaseSingleton.getInstance();
        unbinder = ButterKnife.bind(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(MainActivity.this, DrawerActivity.class));
                    MainActivity.this.finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    mFragmentManager = getSupportFragmentManager();
                    startLoginFragment();
                }
            }
        };
        fireBase.check(mAuthListener);
    }

    @Override
    public void onClickBtnLogin(int code) {
        switch (code) {
            case SING_UP_ID:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.fragment_fade_in,
                                R.anim.fragment_fade_out)
                        .replace(R.id.containerLogin, SingUpFragment.getInstance(null))
                        .commit();
                break;
            case RESET_ID:
                startResetFragment();
                break;
        }
    }

    @Override
    public void onClickBtnSingUp(int response) {
        switch (response) {
            case LOGIN_ID:
                startLoginFragment();
                break;
            case RESET_ID:
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
