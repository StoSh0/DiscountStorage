package com.stosh.discountstorage.drawer.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRoomFragment extends Fragment implements View.OnClickListener {

    private Button buttonCreate;
    private EditText editTextCreateName;
    private EditText editTextCreatePass;
    private View view;
    private FireBaseSingleton fireBase;
    private ListenerCreateRoom listener;

    public CreateRoomFragment() {
    }

    public interface ListenerCreateRoom {
        void responseCreateRoom();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListenerCreateRoom) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements ListenerCreateRoom");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_room, container, false);
        fireBase = FireBaseSingleton.getInstance();
        init();
        return view;
    }

    private void init() {
        buttonCreate = (Button) view.findViewById(R.id.btnCreateRoom);
        editTextCreateName = (EditText) view.findViewById(R.id.editTextCreateRoomName);
        editTextCreatePass = (EditText) view.findViewById(R.id.editTextCreateRoomPass);
        buttonCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        {
            String name = editTextCreateName.getText().toString();
            String password = editTextCreatePass.getText().toString();
            if (TextUtils.isEmpty(name)) {
                editTextCreateName.setError(getString(R.string.email_is_empty));
                return;
            } else if (TextUtils.isEmpty(password)) {
                editTextCreatePass.setError(getString(R.string.password_is_empty));
                return;
            } else if (password.length() < 6) {
                editTextCreateName.setError(getString(R.string.password_to_short));
                return;
            }
            fireBase.createList(name);
            fireBase.createRoom(name, password);
            listener.responseCreateRoom();
        }
    }
}


