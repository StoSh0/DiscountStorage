package com.stosh.discountstorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingProfile extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.editText_settingChangeEmail)
    EditText editTextChangeEmail;
    @BindView(R.id.editText_settingChangePassword)
    EditText editTextChangePassword;
    @BindView(R.id.editText_settingEmailReset)
    EditText editTextEmailReset;
    @BindView(R.id.btn_settingChangeEmail)Button btnChangeEmail;
    @BindView(R.id.btn_settingChangePassword)Button btnChangePassword;
    @BindView(R.id.btn_settingResetEmail)Button btnResetToEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        unbinder = ButterKnife.bind(this);
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
                changeEmail();
                break;
            case R.id.btn_settingChangePassword:
                changePassword();
                break;
            case R.id.btn_settingResetEmail:
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
                break;
            case R.id.btn_singOut:
                singOut();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void changeEmail (){

    }

    private void changePassword (){

    }

    private void resetPasswordToEmail(){

    }

    private void deleteUser (){

    }

    private void singOut (){

    }
}
