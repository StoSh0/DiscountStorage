package com.stosh.discountstorage.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.activities.EditActivity;
import com.stosh.discountstorage.interfaces.Const;

import java.util.HashMap;
import java.util.List;

/**
 * Created by StoSh on 16-Apr-17.
 */

public class ShowRoomListAdapter extends ArrayAdapter {
	
	private int layout;
	private List<HashMap<String, Object>> data;
	private Context context;
	
	public ShowRoomListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HashMap<String, Object>> data) {
		super(context, resource, data);
		this.context = context;
		layout = resource;
		this.data = data;
	}
	
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		convertView = inflater.inflate(layout, parent, false);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.init(convertView, position);
		return convertView;
	}
	
	private class ViewHolder extends AppCompatActivity {
		private TextView textViewName;
		private TextView textViewSub;
		private Button buttonDell;
		
		public void init(View convertView, final int position) {
			final HashMap<String, Object> itemHashMap = data.get(position);
			final String name = itemHashMap.get(Const.NAME).toString();
			final String creator = itemHashMap.get(Const.CREATOR).toString();
			textViewName = (TextView) convertView.findViewById(R.id.textViewNameShowRooms);
			textViewSub = (TextView) convertView.findViewById(R.id.textViewSubShowRooms);
			buttonDell = (Button) convertView.findViewById(R.id.btnDellShowRooms);
			
			buttonDell.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
					fireBase.deleteFromRoomList(itemHashMap.get(Const.ID).toString());
					textViewName.setText("Room was removed, please update list");
					textViewName.setTextSize(20);
				}
			});
			
			textViewName.setText(name);
			textViewSub.setText(creator);
			
		}
	}
}


