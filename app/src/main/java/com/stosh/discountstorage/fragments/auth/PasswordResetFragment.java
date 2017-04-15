package com.stosh.discountstorage.fragments.auth;




import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordResetFragment extends Fragment implements View.OnClickListener, OnCompleteListener{

    private EditText editTextEmail;
    private FireBaseSingleton fireBase;
    private View view;
    private Button buttonReset, buttonBack;
    private ProgressBar progressBar;
    private InputMethodManager inputMethodManager;

    public static PasswordResetFragment getInstance(@Nullable Bundle data) {
        PasswordResetFragment fragment = new PasswordResetFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password_reset, container, false);
        fireBase = FireBaseSingleton.getInstance();
        init();
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        return view;
    }

    private void init() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmailReset);
        buttonReset = (Button) view.findViewById(R.id.btnReset);
        buttonBack = (Button) view.findViewById(R.id.btnResetBack);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarResetPassword);

        buttonReset.setOnClickListener(this);
        buttonBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResetBack:
                getActivity().onBackPressed();
                inputMethodManager.hideSoftInputFromWindow(buttonBack.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.btnReset:
                String email = editTextEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError(getString(R.string.email_is_empty));
                    break;
                }
                fireBase.resetPassword(email, this);
                progressBar.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(buttonReset.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                buttonReset.setClickable(false);
                buttonBack.setClickable(false);
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(
                    getActivity(),
                    getString(R.string.send_reset),
                    Toast.LENGTH_SHORT
            )
                    .show();

            progressBar.setVisibility(View.GONE);
            buttonReset.setClickable(true);
            buttonBack.setClickable(true);
        } else {
            Toast.makeText(getActivity(), getString(R.string.failed_reset), Toast.LENGTH_SHORT)
                    .show();
            progressBar.setVisibility(View.GONE);
            buttonReset.setClickable(true);
            buttonBack.setClickable(true);
        }
    }
}
