package com.stosh.discountstorage.login.fragments;


import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class SingUpFragment extends Fragment implements View.OnClickListener {

    private String TAG = "AUTH";
    private final int LOGIN_ID = 1;
    private final int RESET_ID = 3;
    private View view;
    private Button btnSingUp, btnSingUpFrag, btnResetPass;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FireBaseSingleton fireBase;
    private ListenerFragment listenerFragment;

    public interface ListenerFragment {
        void onClickBtnSingUp(int response);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            listenerFragment = (ListenerFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implements ListenerSingUp");
        }
    }

    public static SingUpFragment getInstance(@Nullable Bundle data){
        SingUpFragment fragment = new SingUpFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sing_up, container, false);
        fireBase = FireBaseSingleton.getInstance();
        init();
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
                btnSingUp.setClickable(false);
                btnResetPass.setClickable(false);
                btnSingUpFrag.setClickable(false);
                createListenerSingUp(email, password);
                break;
            case R.id.btnResetPassFrag:
                listenerFragment.onClickBtnSingUp(RESET_ID);
                break;
            case R.id.btnSingInFrag:
                listenerFragment.onClickBtnSingUp(LOGIN_ID);
                break;
        }
    }

    private void createListenerSingUp(final String email, final String password){
        OnCompleteListener<AuthResult> listenerSingUp = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                    progressBar.setVisibility(View.GONE);
                    fireBase.addUserToDB(email, password);
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
        };
        fireBase.singUp(email, password, listenerSingUp);
    }


}
