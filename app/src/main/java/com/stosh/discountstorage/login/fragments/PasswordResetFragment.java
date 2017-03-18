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
public class PasswordResetFragment extends Fragment {


    private EditText editTextEmail;
    private View view;
    private View.OnClickListener onClickListener;
    private Button buttonReset, buttonBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_password_reset, container, false);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnResetBack:
                        listener.onClickBack();
                        break;
                    case R.id.btnReset:
                        String email = editTextEmail.getText().toString();
                        if (TextUtils.isEmpty(email)){
                            editTextEmail.setError(getString(R.string.email_is_empty));
                            break;
                        }
                        listener.resetPassword(email);
                        break;
                }
            }
        };
        init();
        return view;
    }

    private void init() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmailReset);
        buttonReset = (Button) view.findViewById(R.id.btnReset);
        buttonBack = (Button) view.findViewById(R.id.btnResetBack);

        buttonReset.setOnClickListener(onClickListener);
        buttonBack.setOnClickListener(onClickListener);

    }


    private ListenerReset listener;

    public interface ListenerReset {
        public void onClickBack();

        public void resetPassword(String email);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListenerReset) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implements ListenerReset");
        }
    }
}
