package com.stosh.discountstorage.login.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private String TAG = "AUTH";
    private final int SING_UP_ID = 2;
    private final int RESET_ID = 3;

    private FireBaseSingleton fireBase;
    private View view;
    private Button btnLogin, btnSingUpFrag, btnResetPass;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private ListenerFragment listenerFragment;

    public interface ListenerFragment {
        void onClickBtnLogin(int response);


    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            listenerFragment = (ListenerFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implements ListenerLogin");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        fireBase = FireBaseSingleton.getInstance();
        init();
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
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setClickable(false);
                btnSingUpFrag.setClickable(false);
                btnResetPass.setClickable(false);
                createListenerLogin(email, password);
                break;

            case R.id.btnSingUpFrag:
                listenerFragment.onClickBtnLogin(SING_UP_ID);
                break;

            case R.id.btnResetFrag:
                listenerFragment.onClickBtnLogin(RESET_ID);
                onDestroy();
                break;
        }
    }

    private void createListenerLogin(String email, String password) {
        OnCompleteListener<AuthResult> listenerLogin = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
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
        };
        fireBase.login(getActivity(), email, password, listenerLogin);

    }
}
