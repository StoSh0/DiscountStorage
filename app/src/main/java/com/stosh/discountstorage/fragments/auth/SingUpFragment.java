package com.stosh.discountstorage.fragments.auth;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.AuthFragmentListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingUpFragment extends Fragment implements View.OnClickListener, OnCompleteListener {
	
	private String TAG = "AUTH";
	private View view;
	private Button btnSingUp, btnSingUpFrag, btnResetPass;
	private EditText editTextEmail, editTextPassword;
	private ProgressBar progressBar;
	private FireBaseSingleton fireBase;
	private AuthFragmentListener listener;
	private String email, password;
	private InputMethodManager inputMethodManager;
	
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listener = (AuthFragmentListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() + "must implements ListenerSingUp");
		}
	}
	
	public static SingUpFragment getInstance(@Nullable Bundle data) {
		SingUpFragment fragment = new SingUpFragment();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_auth_sing_up, container, false);
		fireBase = FireBaseSingleton.getInstance();
		init();
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return view;
	}
	
	private void init() {
		editTextEmail = (EditText) view.findViewById(R.id.editTextEmailSingUp);
		editTextPassword = (EditText) view.findViewById(R.id.editTextPasswordSingUp);
		btnSingUp = (Button) view.findViewById(R.id.btnRegister);
		btnSingUpFrag = (Button) view.findViewById(R.id.btnSingInFrag);
		btnResetPass = (Button) view.findViewById(R.id.btnResetPassFrag);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarSingUp);
		btnSingUp.setOnClickListener(this);
		btnSingUpFrag.setOnClickListener(this);
		btnResetPass.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnRegister:
				email = editTextEmail.getText().toString();
				password = editTextPassword.getText().toString();
				if (TextUtils.isEmpty(email)) {
					editTextEmail.setError(getString(R.string.email_is_empty));
					break;
				} else if (TextUtils.isEmpty(password)) {
					editTextPassword.setError(getString(R.string.password_is_empty));
					break;
				} else if (password.length() < 6) {
					editTextPassword.setError(getString(R.string.password_to_short));
					break;
				}
				fireBase.singUp(email, password, this);
				progressBar.setVisibility(View.VISIBLE);
				btnSingUp.setClickable(false);
				btnResetPass.setClickable(false);
				btnSingUpFrag.setClickable(false);
				
				inputMethodManager.hideSoftInputFromWindow(btnSingUp.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case R.id.btnResetPassFrag:
				listener.clickBtn(Const.RESET_ID);
				inputMethodManager.hideSoftInputFromWindow(btnResetPass.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case R.id.btnSingInFrag:
				listener.clickBtn(Const.LOGIN_ID);
				inputMethodManager.hideSoftInputFromWindow(btnSingUpFrag.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				break;
		}
	}
	
	@Override
	public void onComplete(@NonNull Task task) {
		if (task.isSuccessful()) {
			Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
			progressBar.setVisibility(View.GONE);
			fireBase.createUserInDB(email, password);
			btnSingUp.setClickable(true);
			btnResetPass.setClickable(true);
			btnSingUpFrag.setClickable(true);
		} else {
			Toast.makeText(
					getActivity(),
					R.string.auth_failed,
					Toast.LENGTH_SHORT)
					.show();
			progressBar.setVisibility(View.GONE);
			btnSingUp.setClickable(true);
			btnResetPass.setClickable(true);
			btnSingUpFrag.setClickable(true);
		}
	}
}
