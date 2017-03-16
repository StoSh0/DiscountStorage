/*
package com.stosh.discountstorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stosh.discountstorage.Auth.Login;
import com.stosh.discountstorage.Auth.SingUp;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
*/
/**
 * Created by StoSh on 10-Mar-17.
 *//*

public class Main extends AppCompatActivity {


    private Unbinder unbinder;
    private String TAG = "checkAuth";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        unbinder = ButterKnife.bind(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(Main.this, Drawer.class));

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @OnClick({R.id.btn_login_act, R.id.btn_register_act})
    public void OnButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.btn_login_act:
                startActivity(new Intent(this, Login.class));
                finish();
                break;

            case R.id.btn_register_act:
                startActivity(new Intent(this, SingUp.class));
                finish();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
*/
