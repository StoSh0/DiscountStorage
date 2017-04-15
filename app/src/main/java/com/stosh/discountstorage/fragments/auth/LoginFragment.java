package com.stosh.discountstorage.fragments.auth;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class LoginFragment extends Fragment implements View.OnClickListener, OnCompleteListener {

    private String TAG = "AUTH";


    private FireBaseSingleton fireBase;
    private View view;
    private Button btnLogin, btnSingUpFrag, btnResetPass;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private AuthFragmentListener listener;
    private InputMethodManager inputMethodManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AuthFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implements ListenerLogin");
        }
    }

    public static LoginFragment getInstance(@Nullable Bundle data) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        fireBase = FireBaseSingleton.getInstance();
        init();
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        return view;
    }

    private void init() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmailLogin);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPasswordLogin);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnSingUpFrag = (Button) view.findViewById(R.id.btnSingUpFrag);
        btnResetPass = (Button) view.findViewById(R.id.btnResetFrag);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLogin);

        btnLogin.setOnClickListener(this);
        btnSingUpFrag.setOnClickListener(this);
        btnResetPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
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
                fireBase.login(getActivity(), email, password, this);
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setClickable(false);
                btnSingUpFrag.setClickable(false);
                btnResetPass.setClickable(false);
                inputMethodManager.hideSoftInputFromWindow(btnLogin.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                break;

            case R.id.btnSingUpFrag:
                listener.clickBtn(Const.SING_UP_ID);
                inputMethodManager.hideSoftInputFromWindow(btnSingUpFrag.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                break;

            case R.id.btnResetFrag:
                listener.clickBtn(Const.RESET_ID);
                inputMethodManager.hideSoftInputFromWindow(btnResetPass.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                onDestroy();
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
            progressBar.setVisibility(View.GONE);
            btnLogin.setClickable(true);
            btnSingUpFrag.setClickable(true);
            btnResetPass.setClickable(true);
        } else {
            Log.w(TAG, "signInWithEmail:failed", task.getException());
            Toast.makeText(getActivity(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnLogin.setClickable(true);
            btnSingUpFrag.setClickable(true);
            btnResetPass.setClickable(true);
        }
    }
}
