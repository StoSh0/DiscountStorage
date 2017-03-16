package com.stosh.discountstorage.Auth;


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
public class SingUpFragment extends Fragment {


    private final int LOGIN = 1;
    private final int RESET = 3;
    private View view;
    private Button btnSingUp, btnSingUpFrag, btnResetPass;
    private EditText editTextEmail, editTextPassword;
    private View.OnClickListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sing_up, container, false);

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnRegister:
                        String email = editTextEmail.getText().toString();
                        String password = editTextPassword.getText().toString();
                        if (TextUtils.isEmpty(email)){
                            editTextEmail.setError(getString(R.string.email_is_empty));
                            break;
                        }else if (TextUtils.isEmpty(password)){
                            editTextPassword.setError(getString(R.string.password_is_empty));
                            break;
                        }else if (password.length() <6){
                            editTextPassword.setError(getString(R.string.password_to_short));
                        }
                        listener.singUp(email,password);

                        break;
                    case R.id.btnResetPassFrag:
                        listener.onClickBtnSingUp(RESET);
                        break;
                    case R.id.btnSingInFrag:
                        listener.onClickBtnSingUp(LOGIN);
                        break;
                }
            }
        };

        init();
        return view;
    }

    private void init() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmailSingUp);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPasswordSingUp);
        btnSingUp = (Button) view.findViewById(R.id.btnRegister);
        btnSingUpFrag = (Button) view.findViewById(R.id.btnSingInFrag);
        btnResetPass = (Button) view.findViewById(R.id.btnResetPassFrag);

        btnSingUp.setOnClickListener(clickListener);
        btnSingUpFrag.setOnClickListener(clickListener);
        btnResetPass.setOnClickListener(clickListener);
    }

    private ListenerSingUp listener;

    public interface ListenerSingUp {
        public void onClickBtnSingUp(int code);

        public void singUp(String email, String password);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListenerSingUp) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements OnClicBtnListener");
        }
    }
}
