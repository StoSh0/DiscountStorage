package com.stosh.discountstorage.activities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.fragments.settings.ChangeEmailFragment;
import com.stosh.discountstorage.fragments.settings.ChangePasswordFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class SettingProfileActivity extends AppCompatActivity implements OnCompleteListener {
	
	private FragmentManager mFragmentManager;
	private FireBaseSingleton fireBase;
	private Unbinder unbinder;
	
	@BindView(R.id.progressBarSetting)
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_profile);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
				progressBar.setVisibility(View.VISIBLE);
				break;
			case R.id.btnSingOut:
				fireBase.singOut();
				startActivity(new Intent(this, MainActivity.class));
				finish();
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
		if (task.isSuccessful()) {
			progressBar.setVisibility(View.GONE);
			fireBase.singOut();
			startActivity(new Intent(SettingProfileActivity.this, MainActivity.class));
			finish();
		}
	}
}
