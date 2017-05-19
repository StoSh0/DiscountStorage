package com.stosh.discountstorage.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.Const;

import butterknife.BindView;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {
	private FireBaseSingleton fireBase;
	private String id;
	@BindView(R.id.editTextEdit)
	EditText name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		fireBase = FireBaseSingleton.getInstance();
		id = getIntent().getStringExtra(Const.ID);
	}
	
	
	@OnClick({R.id.btnEdit, R.id.btnEditDelete})
	public void onButtonClick(Button button) {
		switch (button.getId()){
			case R.id.btnEdit:
				break;
			case R.id.btnEditDelete:
				fireBase.deleteRoom(id);
				fireBase.deleteFromRoomList(id);
				Toast.makeText(EditActivity.this, getString(R.string.room_remove), Toast.LENGTH_LONG).show();
				break;
		}
	}
}
