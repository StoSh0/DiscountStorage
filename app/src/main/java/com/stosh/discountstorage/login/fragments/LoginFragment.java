package com.stosh.discountstorage.login.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.stosh.discountstorage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private final int SING_UP_ID = 2;
    private final int RESET_ID = 3;

    private View view;
    private Button btnLogin, btnSingUpFrag, btnResetPass;
    private EditText editTextEmail, editTextPassword;
    private View.OnClickListener clickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);


        clickListener = new View.OnClickListener() {
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
                        listener.login(email, password);

                        break;

                    case R.id.btnSingUpFrag:
                        listener.onClickBtnLogin(SING_UP_ID);
                        break;

                    case R.id.btnResetFrag:
                        listener.onClickBtnLogin(RESET_ID);
                        onDestroy();
                        break;
                }
            }
        };
        init();
        return view;
    }

    private void init() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmailLogin);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPasswordLogin);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnSingUpFrag = (Button) view.findViewById(R.id.btnSingUpFrag);
        btnResetPass = (Button) view.findViewById(R.id.btnResetFrag);

        btnLogin.setOnClickListener(clickListener);
        btnSingUpFrag.setOnClickListener(clickListener);
        btnResetPass.setOnClickListener(clickListener);
    }

    public interface ListenerLogin {
        public void onClickBtnLogin(int code);

        public void login(String email, String password);
    }

    private ListenerLogin listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListenerLogin) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements ListenerLogin");
        }
    }
}
