package com.stosh.discountstorage.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.Const;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditActivity extends AppCompatActivity {
	private FireBaseSingleton fireBase;
	private String idCard;
	private String idRoom;
	private Unbinder unbinder;
	@BindView(R.id.editTextEdit)
	EditText name;
	@BindView(R.id.progressBarEdit)
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		unbinder = ButterKnife.bind(this);
		fireBase = FireBaseSingleton.getInstance();
		idCard = getIntent().getStringExtra(Const.ID);
		idRoom = getIntent().getStringExtra(Const.ID_ROOM_LIST);
	}
	
	
	@OnClick({R.id.btnEdit, R.id.btnEditDelete})
	public void onButtonClick(Button button) {
		progressBar.setVisibility(View.VISIBLE);
		switch (button.getId()) {
			case R.id.btnEdit:
				
				String name = this.name.getText().toString();
				if (name == null) {
					this.name.setError(getString(R.string.enter_name));
					break;
				}
				fireBase.reNameCard(idCard, name);
				Toast.makeText(this, "Name was change", Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.GONE);
				finish();
				break;
			case R.id.btnEditDelete:
				fireBase.deleteCard(idCard);
				Log.d("qwerty", idCard);
				fireBase.deleteFromCardList(idRoom,idCard);
				Toast.makeText(this, "Name was change", Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.GONE);
				finish();
				break;
		}
		
	}
}
