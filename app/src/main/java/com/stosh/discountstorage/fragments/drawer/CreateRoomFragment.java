package com.stosh.discountstorage.fragments.drawer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private InputMethodManager inputMethodManager;

    public static CreateRoomFragment getInstance(@Nullable Bundle data) {
        CreateRoomFragment fragment = new CreateRoomFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_room, container, false);
        fireBase = FireBaseSingleton.getInstance();
        init();
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
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
                editTextCreatePass.setError(getString(R.string.password_to_short));
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(buttonCreate.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            fireBase.createRoomList(name);
            fireBase.createRoom(name, password);
            Toast.makeText(getActivity(), getString(R.string.room_was_create), Toast.LENGTH_LONG).show();
        }
    }
}


