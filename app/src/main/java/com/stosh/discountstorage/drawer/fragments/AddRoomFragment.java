package com.stosh.discountstorage.drawer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class AddRoomFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText editTextCreatorEmail, editTextNameRoom, editTextPasswordRoom;
    private Button buttonCancel, buttonAdd;
    private FireBaseSingleton fireBase;

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

        buttonCancel = (Button) view.findViewById(R.id.btnCancelAddRoom);
        buttonAdd = (Button) view.findViewById(R.id.btnAddRoom);

        buttonCancel.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        fireBase = FireBaseSingleton.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRoom:
                String creatorName = editTextCreatorEmail.getText().toString();
                String roomName = editTextNameRoom.getText().toString();
                String password = editTextPasswordRoom.getText().toString();
                if (TextUtils.isEmpty(creatorName)) {
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
                creatorName = creatorName.replace(".", "").toLowerCase();
                checkInData(creatorName, roomName, password);
                break;
            case R.id.btnCancelAddRoom:
                break;
        }
    }

    private void checkInData(final String creator, final String name, final String password){
        Log.d("1", ":dsaada");
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if (user == null){
                    Toast.makeText(getActivity(), "User Not Found",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("1", user.email);
                checkRoom(creator, name,password);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("1", "Cancel");
            }
        };
        fireBase.checkUserInDB(creator, listener);
    }

    private void checkRoom(final String creator, final String nameRoom, final String password){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoomList roomList = dataSnapshot.getValue(RoomList.class);
                if (roomList == null){
                    Toast.makeText(getActivity(), "Room Not Found",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("1", roomList.name);
                checkPassword(creator, nameRoom, password);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        fireBase.checkRoomList(creator, nameRoom, listener);
    }

    private void checkPassword(String creator, final String nameRoom, final String password){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Room room = dataSnapshot.getValue(Room.class);
                if (room == null){
                    Toast.makeText(getActivity(), "Room Not Found",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("1", room.password);
                Log.d("1", password);
                if (room.password.equals(password)){
                    fireBase.createRoomList(nameRoom);
                    return;
                }
                Toast.makeText(getActivity(), "Password invalid",Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        fireBase.checkPassword(creator, nameRoom,listener);

    }
}
