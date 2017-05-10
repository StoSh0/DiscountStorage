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
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.database.Room;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRoomFragment extends Fragment implements View.OnClickListener {
	
	private View view;
	private EditText editTextCreatorEmail, editTextNameRoom, editTextPasswordRoom;
	private FireBaseSingleton fireBase;
	private String password;
	private String id;
	private ProgressBar progressBar;
	private Button buttonAdd;
	private InputMethodManager inputMethodManager;
	
	public static AddRoomFragment getInstance(@Nullable Bundle data) {
		AddRoomFragment fragment = new AddRoomFragment();
		fragment.setArguments(data == null ? new Bundle() : data);
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_drawer_add_room, container, false);
		init();
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return view;
	}
	
	private void init() {
		editTextCreatorEmail = (EditText) view.findViewById(R.id.editTextCreatorEmail);
		editTextNameRoom = (EditText) view.findViewById(R.id.editTextNameRoom);
		editTextPasswordRoom = (EditText) view.findViewById(R.id.editTextPasswordRoom);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBarAddRoom);
		
		buttonAdd = (Button) view.findViewById(R.id.btnAddRoom);
		buttonAdd.setOnClickListener(this);
		fireBase = FireBaseSingleton.getInstance();
	}
	
	@Override
	public void onClick(View v) {
		String creator = editTextCreatorEmail.getText().toString();
		String roomName = editTextNameRoom.getText().toString();
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
		
		
		inputMethodManager.hideSoftInputFromWindow(buttonAdd.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		id = creator.toLowerCase().replace(".", "");
		id = roomName + "_" + id;
		checkInData();
	}
	
	private void checkInData() {
		final ValueEventListener listener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				
				Room room = dataSnapshot.getValue(Room.class);
				if (room == null){
					progressBar.setVisibility(View.GONE);
					Toast.makeText(getActivity(), getString(R.string.data_incorrect), Toast.LENGTH_LONG).show();
					return;
				}
				if (!password.equals(room.password)) {
					progressBar.setVisibility(View.GONE);
					Toast.makeText(getActivity(), getString(R.string.password_incorrect), Toast.LENGTH_LONG).show();
					return;
				}
				progressBar.setVisibility(View.GONE);
				fireBase.addRoomToList(id, room.name, room.creator);
				Toast.makeText(getActivity(), getString(R.string.room_added), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
				
			}
		};
		fireBase.checkRoom(id, listener);
	}
}