package com.stosh.discountstorage.fragments.drawer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.database.Room;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.database.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRoomFragment extends Fragment implements View.OnClickListener, ValueEventListener {

    private View view;
    private EditText editTextCreatorEmail, editTextNameRoom, editTextPasswordRoom;
    private Button buttonAdd;
    private ProgressBar progressBar;
    private FireBaseSingleton fireBase;
    private String creator, roomName, password;

    public static AddRoomFragment getInstance(@Nullable Bundle data) {
        AddRoomFragment fragment = new AddRoomFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_room, container, false);
        init();
        return view;
    }

    private void init() {
        editTextCreatorEmail = (EditText) view.findViewById(R.id.editTextCreatorEmail);
        editTextNameRoom = (EditText) view.findViewById(R.id.editTextNameRoom);
        editTextPasswordRoom = (EditText) view.findViewById(R.id.editTextPasswordRoom);
        buttonAdd = (Button) view.findViewById(R.id.btnAddRoom);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarAddRoom);
        buttonAdd.setOnClickListener(this);
        fireBase = FireBaseSingleton.getInstance();
    }

    @Override
    public void onClick(View v) {
        creator = editTextCreatorEmail.getText().toString();
        roomName = editTextNameRoom.getText().toString();
        password = editTextPasswordRoom.getText().toString();
        if (TextUtils.isEmpty(creator)) {
            editTextCreatorEmail.setError(getString(R.string.email_is_empty));
            return;
        } else if (TextUtils.isEmpty(roomName)) {
            editTextNameRoom.setError(getString(R.string.name));
            return;
        } else if (TextUtils.isEmpty(password)) {
            editTextPasswordRoom.setError(getString(R.string.password_is_empty));
            return;
        } else if (password.length() < 6) {
            editTextPasswordRoom.setError(getString(R.string.password_to_short));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        creator = creator.replace(".", "").toLowerCase();
        fireBase.checkUserInDB(creator,roomName, this);
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (user == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "User Not Found", Toast.LENGTH_LONG).show();
            return;
        }
        RoomList roomList = dataSnapshot.getValue(RoomList.class);
        if (roomList == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Room Not Found", Toast.LENGTH_LONG).show();
            return;
        }
        Room room = dataSnapshot.getValue(Room.class);
        if (room.password.equals(password)) {
            fireBase.addToRoomList(creator, roomName);
            progressBar.setVisibility(View.GONE);
            return;
        }
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Password invalid", Toast.LENGTH_LONG).show();
        return;
    }


    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
