package com.stosh.discountstorage.login;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stosh.discountstorage.drawer.AllActivity;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.login.fragments.LoginFragment;
import com.stosh.discountstorage.login.fragments.PasswordResetFragment;
import com.stosh.discountstorage.login.fragments.SingUpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoginActivity extends AppCompatActivity implements LoginFragment.ListenerLogin, SingUpFragment.ListenerSingUp, PasswordResetFragment.ListenerReset {

    private final int LOGIN_ID = 1;
    private final int SING_UP_ID = 2;
    private final int RESET_ID = 3;
    private final int BACK_ID = 4;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private String TAG = "Auth";
    private Unbinder unbinder;


    @BindView(R.id.progressBarLogin)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        unbinder = ButterKnife.bind(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LoginActivity.this, AllActivity.class));
                    finish();

                } else {
                    fragmentTransaction = getFragmentManager().beginTransaction();
                    fragment = new LoginFragment();
                    fragmentTransaction.replace(R.id.containerLogin, fragment).commit();
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

    }


    @Override
    public void login(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, AllActivity.class));
                            finish();

                        } else {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
        ;
    }

    @Override
    public void singUp(final String email, final String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    progressBar.setVisibility(View.GONE);
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void resetPassword(String email) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, getString(R.string.send_reset), Toast.LENGTH_SHORT)
                            .show();

                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            getString(R.string.failed_reset),
                            Toast.LENGTH_SHORT
                    ).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClickBtnLogin(int code) {
        switch (code) {
            case SING_UP_ID:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new SingUpFragment();
                fragmentTransaction.replace(R.id.containerLogin, fragment).commit();
                break;
            case RESET_ID:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new PasswordResetFragment();
                fragmentTransaction.replace(R.id.containerLogin, fragment).addToBackStack(null).commit();
                break;
        }

    }

    @Override
    public void onClickBtnSingUp(int code) {
        switch (code) {
            case LOGIN_ID: {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new LoginFragment();
                fragmentTransaction.replace(R.id.containerLogin, fragment).commit();
                break;
            }
            case RESET_ID: {
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new PasswordResetFragment();
                fragmentTransaction.replace(R.id.containerLogin, fragment).addToBackStack(null).commit();
                break;
            }
        }
    }



    @Override
    public void onClickBack() {
        onBackPressed();
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
