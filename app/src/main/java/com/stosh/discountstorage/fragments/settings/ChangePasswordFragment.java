package com.stosh.discountstorage.fragments.settings;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.activities.MainActivity;

import static android.widget.Toast.makeText;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener, OnCompleteListener {

    private View view;
    private EditText editTextPassword;
    private Button buttonChange;
    private ProgressBar progressBar;
    private FireBaseSingleton fireBase;

    public static ChangePasswordFragment getInstance(@Nullable Bundle data) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings_change_password, container, false);
        init();
        fireBase = FireBaseSingleton.getInstance();
        return view;
    }

    private void init() {
        editTextPassword = (EditText) view.findViewById(R.id.editTextSettingChangePassword);
        buttonChange = (Button) view.findViewById(R.id.btnSettingChangePassword);
        buttonChange.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarSettingPassword);
    }

    @Override
    public void onClick(View v) {
        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.password_is_empty));
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.password_to_short));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        fireBase.changePassword(password, this);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            makeText(
                    getActivity(),
                    getString(R.string.email_update),
                    Toast.LENGTH_LONG
            ).show();
            fireBase.singOut();
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
            progressBar.setVisibility(View.GONE);
        } else {
            makeText(
                    getActivity(),
                    getString(R.string.failed_update_email),
                    Toast.LENGTH_LONG)
                    .show();
            progressBar.setVisibility(View.GONE);
        }
    }
}

