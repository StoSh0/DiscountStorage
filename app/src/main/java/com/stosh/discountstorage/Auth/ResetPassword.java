package com.stosh.discountstorage.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.stosh.discountstorage.Drawer;
import com.stosh.discountstorage.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ResetPassword extends AppCompatActivity {

    private FirebaseAuth auth;
    private Unbinder unbinder;

    @BindView(R.id.editText_email_Reset)
    EditText editTextEmailReset;
    @BindView(R.id.progressBarReset)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        unbinder = ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.btn_reset, R.id.btn_reset_back})
    public void OnButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.btn_reset:
                reset();
                break;

            case R.id.btn_reset_back:
                startActivity(new Intent(this, Drawer.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void reset(){
        String email = editTextEmailReset.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmailReset.setError(getString(R.string.email_isEmpty));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(ResetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}

