package com.stosh.discountstorage.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.interfaces.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by StoSh on 16-Apr-17.
 **/

public class ShowRoomListAdapter extends ArrayAdapter implements ValueEventListener {
	
	private int resource;
	private List<HashMap<String, Object>> data;
	private Context context;
	private FireBaseSingleton fireBase;
	private String creator;
	private String id;
	
	public ShowRoomListAdapter(
			@NonNull Context context,
			@LayoutRes int resource,
			@NonNull List<HashMap<String, Object>> data
	) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
	}
	
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(context, resource, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.init(convertView, position);
		return convertView;
	}
	
	@Override
	public void onDataChange(DataSnapshot dataSnapshot) {
		ArrayList<String> cardList = new ArrayList<>();
		Log.d("qwerty", "cccccc" + dataSnapshot);
		fireBase.deleteRoom(id);
		fireBase.deleteFromRoomList(id);
		for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
			RoomList roomList = roomsDataSnapshot.getValue(RoomList.class);
			cardList.add(roomList.ID);
		}
		for (String id : cardList) {
			fireBase.deleteCard(id);
		}
	}
	
	@Override
	public void onCancelled(DatabaseError databaseError) {
	}
	
	private class ViewHolder extends AppCompatActivity {
		private TextView textViewName;
		private TextView textViewSub;
		private Button buttonDell;
		
		public void init(View convertView, final int position) {
			fireBase = FireBaseSingleton.getInstance();
			HashMap<String, Object> itemHashMap = data.get(position);
			String name = itemHashMap.get(Const.NAME).toString();
			creator = itemHashMap.get(Const.CREATOR).toString();
			id = itemHashMap.get(Const.ID).toString();
			textViewName = (TextView) convertView.findViewById(R.id.textViewNameShowRooms);
			textViewSub = (TextView) convertView.findViewById(R.id.textViewSubShowRooms);
			buttonDell = (Button) convertView.findViewById(R.id.btnDellShowRooms);
			buttonDell.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!creator.equals(fireBase.getUserEmail())) {
						fireBase.deleteFromRoomList(id);
						textViewName.setText(getString(R.string.room_remove));
						textViewName.setTextSize(20);
					} else {
						fireBase.getCardList(id, ShowRoomListAdapter.this);
						textViewName.setText(context.getString(R.string.room_remove));
						textViewName.setTextSize(20);
						buttonDell.setVisibility(View.GONE);
					}
				}
			});
			textViewName.setText(name);
			textViewSub.setText(creator);
		}
	}
}


