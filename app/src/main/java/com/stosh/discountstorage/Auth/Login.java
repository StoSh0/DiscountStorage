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
import com.stosh.discountstorage.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.stosh.discountstorage.R.id.editText_email_Login;
import static com.stosh.discountstorage.R.id.editText_password_Login;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Unbinder unbinder;
    private String TAG = "checkAuth";

    @BindView(editText_email_Login)
    EditText editTextEmail;
    @BindView(editText_password_Login)
    EditText editTextPassword;
    @BindView(R.id.progressBar_login)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.btn_login, R.id.btn_reset_act, R.id.btn_singUp_act})
    public void OnButtonClick(Button button) {
        switch (button.getId()) {

            case R.id.btn_login:
                logIn();
                break;

            case R.id.btn_reset_act:
                startActivity(new Intent(this, ResetPassword.class));
                break;

            case R.id.btn_singUp_act:
                startActivity(new Intent(this, SingUp.class));
                finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void logIn() {
        String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(R.string.email_isEmpty + "");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(R.string.password_isEmpty + "");
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            if (password.length() < 6) {
                                editTextPassword.setError(R.string.password_toShort + "");
                            }
                            Toast.makeText(Login.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
