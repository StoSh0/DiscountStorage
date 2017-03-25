package com.stosh.discountstorage.settings;

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
import com.google.firebase.auth.FirebaseUser;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.login.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.widget.Toast.makeText;

public class SettingProfileActivity extends AppCompatActivity {

    private FireBaseSingleton fireBase;
    private Unbinder unbinder;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String email;
    private String password;


    @BindView(R.id.editText_settingChangeEmail)
    EditText editTextChangeEmail;
    @BindView(R.id.editText_settingChangePassword)
    EditText editTextChangePassword;
    @BindView(R.id.editText_settingEmailReset)
    EditText editTextEmailReset;
    @BindView(R.id.progressBar_setting)
    ProgressBar progressBar;
    @BindView(R.id.btn_settingChangeEmail)
    Button btnChangeEmail;
    @BindView(R.id.btn_settingChangePassword)
    Button btnChangePassword;
    @BindView(R.id.btn_settingResetEmail)
    Button btnResetToEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        unbinder = ButterKnife.bind(this);
        fireBase = FireBaseSingleton.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @OnClick({
            R.id.btn_settingChangeEmail,
            R.id.btn_settingChangePassword,
            R.id.btn_settingResetEmail,
            R.id.btn_settingChangeEmailVis,
            R.id.btn_settingChangePasswordVis,
            R.id.btn_settingResetEmailVis,
            R.id.btn_deleteUser,
            R.id.btn_singOut


    })
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.btn_settingChangeEmail:
                email = editTextChangeEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    editTextChangeEmail.setError(getString(R.string.email_is_empty));
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                changeEmail();

                break;
            case R.id.btn_settingChangePassword:
                password = editTextChangePassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    editTextChangePassword.setError(getString(R.string.password_is_empty));
                    return;
                }
                if (password.length() < 6) {
                    editTextChangePassword.setError(getString(R.string.password_to_short));
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                changePassword();
                break;
            case R.id.btn_settingResetEmail:
                email = editTextEmailReset.getText().toString();
                if (TextUtils.isEmpty(email)){
                    editTextEmailReset.setError(getString(R.string.email_is_empty));
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                resetPasswordToEmail();
                break;
            case R.id.btn_settingChangeEmailVis:
                editTextChangePassword.setVisibility(View.GONE);
                btnChangePassword.setVisibility(View.GONE);
                editTextEmailReset.setVisibility(View.GONE);
                btnResetToEmail.setVisibility(View.GONE);

                editTextChangeEmail.setVisibility(View.VISIBLE);
                btnChangeEmail.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_settingChangePasswordVis:
                editTextChangeEmail.setVisibility(View.GONE);
                btnChangeEmail.setVisibility(View.GONE);
                editTextEmailReset.setVisibility(View.GONE);
                btnResetToEmail.setVisibility(View.GONE);

                editTextChangePassword.setVisibility(View.VISIBLE);
                btnChangePassword.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_settingResetEmailVis:
                editTextChangeEmail.setVisibility(View.GONE);
                btnChangeEmail.setVisibility(View.GONE);
                editTextChangePassword.setVisibility(View.GONE);
                btnChangePassword.setVisibility(View.GONE);

                editTextEmailReset.setVisibility(View.VISIBLE);
                btnResetToEmail.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_deleteUser:
                deleteUser();
                finish();
                break;
            case R.id.btn_singOut:
                singOut();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void changeEmail() {
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    makeText(
                            SettingProfileActivity.this,
                            getString(R.string.email_update),
                            Toast.LENGTH_LONG
                    ).show();
                    singOut();
                    progressBar.setVisibility(View.GONE);
                } else {
                    makeText(
                            SettingProfileActivity.this,
                            getString(R.string.failed_update_email),
                            Toast.LENGTH_LONG
                    ).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void changePassword() {
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            makeText(
                                    SettingProfileActivity.this,
                                    getString(R.string.email_update),
                                    Toast.LENGTH_LONG
                            ).show();
                            singOut();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            makeText(
                                    SettingProfileActivity.this,
                                    getString(R.string.failed_update_email),
                                    Toast.LENGTH_LONG)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void resetPasswordToEmail() {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            makeText(
                                    SettingProfileActivity.this,
                                    getString(R.string.reset),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            makeText(
                                    SettingProfileActivity.this,
                                    getString(R.string.failed_reset),
                                    Toast.LENGTH_SHORT
                            ).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
        ;
    }

    private void deleteUser() {
        user.delete();
        singOut();
    }

    private void singOut() {
        mAuth.signOut();
        startActivity(new Intent(SettingProfileActivity.this, MainActivity.class));
        finish();
    }
}
