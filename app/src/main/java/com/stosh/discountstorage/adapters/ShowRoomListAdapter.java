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

import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by StoSh on 16-Apr-17.
 */

public class ShowRoomListAdapter extends ArrayAdapter {
	
	private int resource;
	private List<HashMap<String, Object>> data;
	private Context context;
	private HashMap<String, Object> itemHashMap;
	private FireBaseSingleton fireBase;
	private DrawerFragmentListener listener;
	
	public ShowRoomListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HashMap<String, Object>> data, DrawerFragmentListener listener) {
		super(context, resource, data);
		this.context = context;
		this.resource = resource;
		this.data = data;
		this.listener = listener;
	}
	
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(context,resource , null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.init(convertView, position);
		return convertView;
	}
	
	private class ViewHolder extends AppCompatActivity {
		private TextView textViewName;
		private TextView textViewSub;
		private Button buttonDell;
		
		public void init(View convertView, final int position) {
			fireBase = FireBaseSingleton.getInstance();
			itemHashMap = data.get(position);
			String name = itemHashMap.get(Const.NAME).toString();
			String creator = itemHashMap.get(Const.CREATOR).toString();
			textViewName = (TextView) convertView.findViewById(R.id.textViewNameShowRooms);
			textViewSub = (TextView) convertView.findViewById(R.id.textViewSubShowRooms);
			buttonDell = (Button) convertView.findViewById(R.id.btnDellShowRooms);
			if (!creator.equals(fireBase.getUserEmail())){
				buttonDell.setVisibility(View.GONE);
				
			}
			buttonDell.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					listener.send(Const.EDIT_ROOM, itemHashMap.get(Const.ID).toString());
					
				}
			});
			textViewName.setText(name);
			textViewSub.setText(creator);
			
		}
	}
}


