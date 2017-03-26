package com.stosh.discountstorage.settings;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.login.MainActivity;
import com.stosh.discountstorage.settings.fragments.ChangeEmailFragment;
import com.stosh.discountstorage.settings.fragments.ChangePasswordFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.widget.Toast.makeText;

public class SettingProfileActivity extends AppCompatActivity {

    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private FireBaseSingleton fireBase;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        unbinder = ButterKnife.bind(this);
        fireBase = FireBaseSingleton.getInstance();
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
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new ChangeEmailFragment();
                fragmentTransaction.replace(R.id.containerSettings, fragment).commit();
                break;
            case R.id.btnSettingChangePasswordVis:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new ChangePasswordFragment();
                fragmentTransaction.replace(R.id.containerSettings, fragment).commit();
                break;
            case R.id.btnDeleteUser:
                OnCompleteListener listener = new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SettingProfileActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                };
                fireBase.deleteUser(listener);
                break;
            case R.id.btnSingOut:
                fireBase.singOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
