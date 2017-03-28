package com.stosh.discountstorage.drawer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class AddRoomFragment extends Fragment implements View.OnClickListener{

    private View view;
    private EditText editTextCreatorEmail, editTextNameRoom, editTextPasswordRoom;
    private Button buttonCancel, buttonAdd;

    public static AddRoomFragment getInstance(@Nullable Bundle data) {
        AddRoomFragment fragment = new AddRoomFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_add_room, container, false);

        init();
        return view;
    }

    private void init(){
        editTextCreatorEmail = (EditText) view.findViewById(R.id.editTextCreatorEmail);
        editTextNameRoom = (EditText) view.findViewById(R.id.editTextNameRoom);
        editTextPasswordRoom = (EditText) view.findViewById(R.id.editTextPasswordRoom);

        buttonCancel = (Button) view.findViewById(R.id.btnCancelAddRoom);
        buttonAdd = (Button) view.findViewById(R.id.btnAddRoom);

        buttonCancel.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddRoom:
                String creatorName = editTextCreatorEmail.getText().toString();
                String roomName = editTextNameRoom.getText().toString();
                String password = editTextPasswordRoom.getText().toString();
                if (TextUtils.isEmpty(creatorName)){
                    editTextCreatorEmail.setError(getString(R.string.email_is_empty));
                    return;
                }else if (TextUtils.isEmpty(roomName)){
                    editTextNameRoom.setError(getString(R.string.name));
                    return;
                }else if (TextUtils.isEmpty(password)){
                    editTextPasswordRoom.setError(getString(R.string.password_is_empty));
                    return;
                }
                else if (password.length() < 6) {
                    editTextPasswordRoom.setError(getString(R.string.password_to_short));
                    return;
                }
                break;
            case R.id.btnCancelAddRoom:
                break;
        }
    }
}
