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
public class ChangeEmailFragment extends Fragment implements View.OnClickListener, OnCompleteListener {

    private View view;
    private EditText editTextEmail;
    private Button buttonChange;
    private ProgressBar progressBar;
    private FireBaseSingleton fireBase;

    public static ChangeEmailFragment getInstance(@Nullable Bundle data) {
        ChangeEmailFragment fragment = new ChangeEmailFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_change_email, container, false);
        init();
        fireBase = FireBaseSingleton.getInstance();
        return view;
    }

    private void init() {
        editTextEmail = (EditText) view.findViewById(R.id.editTextSettingChangeEmail);
        buttonChange = (Button) view.findViewById(R.id.btnSettingChangeEmail);
        buttonChange.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarSettingEmail);
    }

    @Override
    public void onClick(View v) {
        String email;
        email = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.email_is_empty));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        fireBase.changeEmail(email, this);
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
            progressBar.setVisibility(View.GONE);
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            makeText(
                    getActivity(),
                    getString(R.string.failed_update_email),
                    Toast.LENGTH_LONG
            ).show();
            progressBar.setVisibility(View.GONE);
        }
    }
}
