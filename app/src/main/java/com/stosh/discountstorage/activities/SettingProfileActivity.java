package com.stosh.discountstorage.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.fragments.settings.ChangeEmailFragment;
import com.stosh.discountstorage.fragments.settings.ChangePasswordFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SettingProfileActivity extends AppCompatActivity implements OnCompleteListener {

    private FragmentManager mFragmentManager;
    private FireBaseSingleton fireBase;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        unbinder = ButterKnife.bind(this);
        fireBase = FireBaseSingleton.getInstance();
        mFragmentManager = getSupportFragmentManager();
    }

    @OnClick({
            R.id.btnSettingChangeEmailVis,
            R.id.btnSettingChangePasswordVis,
            R.id.btnDeleteUser,
            R.id.btnSingOut
    })
    public void onButtonClick(Button button) {

        switch (button.getId()) {
            case R.id.btnSettingChangeEmailVis:
                mFragmentManager.beginTransaction()
                        .replace(R.id.containerSettings, ChangeEmailFragment.getInstance(null))
                        .commit();
                break;
            case R.id.btnSettingChangePasswordVis:
                mFragmentManager.beginTransaction()
                        .replace(R.id.containerSettings, ChangePasswordFragment.getInstance(null))
                        .commit();
                break;
            case R.id.btnDeleteUser:
                fireBase.deleteUser(this);
                break;
            case R.id.btnSingOut:
                fireBase.singOut();
                startActivity(new Intent(this, MainActivity.class));
                SettingProfileActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onComplete(@NonNull Task task) {
        startActivity(new Intent(SettingProfileActivity.this, MainActivity.class));
        finish();
    }
}
