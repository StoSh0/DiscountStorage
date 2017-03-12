package com.stosh.discountstorage.Auth;

import android.content.Context;
import android.content.Intent;
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
import com.stosh.discountstorage.Drawer;
import com.stosh.discountstorage.R;

/**
 * Created by StoSh on 12-Mar-17.
 */

public class Sing extends AppCompatActivity {

    private Context context;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private ProgressBar progressBar;
    private String TAG = "checkAuth";
    private FirebaseAuth.AuthStateListener mAuthListener;


    public void setContext(Context context) {

        this.context = context;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;

    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setProgressBar(ProgressBar progressBar) {

        this.progressBar = progressBar;
    }




    public void checkSing() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    context.startActivity(new Intent(context, Login.class));
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void onStart(FirebaseAuth mAuth) {
        mAuth.addAuthStateListener(mAuthListener);
        //
    }

    public void onStop(FirebaseAuth mAuth) {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void login() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(Sing.this, Drawer.class));
                            finish();

                        } else {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Sing.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
        ;

    }

    public void singUp() {

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            //login();
                        } else {
                            Toast.makeText(context, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
        ;
    }
}
