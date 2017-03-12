package com.stosh.discountstorage.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.stosh.discountstorage.Drawer;
import com.stosh.discountstorage.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SingUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Unbinder unbinder;
    private String TAG = "checkAuth";
    private String email;
    private String password;


    @BindView(R.id.editText_email_SingUp)
    EditText editTextEmail;
    @BindView(R.id.editText_password_SingUp)
    EditText editTextPassword;
    @BindView(R.id.progressBarReg)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        unbinder = ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }


    @OnClick({R.id.btn_register, R.id.btn_resetPass_act, R.id.btn_singIn_act})
    public void onButtonClick(Button button) {
        switch (button.getId()) {

            case R.id.btn_register:
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError(getString(R.string.email_isEmpty));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError(getString(R.string.password_isEmpty));
                    return;
                }
                if (password.length() < 6) {
                    editTextPassword.setError(getString(R.string.password_toShort));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                singUp();
                break;
            case R.id.btn_resetPass_act:
                startActivity(new Intent(this, ResetPassword.class));
                break;

            case R.id.btn_singIn_act:
                startActivity(new Intent(this, Login.class));
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void singUp() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            login();
                        } else {
                            Toast.makeText(SingUp.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
        ;
    }

    private void login(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SingUp.this, Drawer.class));
                                finish();

                            }
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(SingUp.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                })
        ;
    }
}
