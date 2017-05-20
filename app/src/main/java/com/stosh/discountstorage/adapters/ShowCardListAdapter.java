package com.stosh.discountstorage.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ShowCardListAdapter extends ArrayAdapter {
	private Context context;
	private int recourse;
	private List<HashMap<String, Object>> data;
	private FireBaseSingleton fireBase;
	private HashMap<String, Object> hashMap;
	private DrawerFragmentListener listener;
	
	public ShowCardListAdapter(@NonNull Context context, @LayoutRes int recourse, @NonNull List<HashMap<String, Object>> data, DrawerFragmentListener listener) {
		super(context, recourse, data);
		this.recourse = recourse;
		this.context = context;
		this.data = data;
		this.listener = listener;
	}
	
	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(context, recourse, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.init(convertView, position);
		return convertView;
	}
	
	private class ViewHolder {
		TextView textViewName;
		TextView textViewCategory;
		Button buttonEdit;
		
		public void init(View convertView, final int position) {
			fireBase = FireBaseSingleton.getInstance();
			hashMap = data.get(position);
			String name = hashMap.get(Const.NAME).toString();
			String category = hashMap.get(Const.CAT).toString();
			String creator = hashMap.get(Const.CREATOR).toString();
			final String idRoom  = hashMap.get(Const.ID_ROOM_LIST).toString();
			final String id = hashMap.get(Const.ID).toString();
			textViewName = (TextView) convertView.findViewById(R.id.textViewNameShowCards);
			textViewCategory = (TextView) convertView.findViewById(R.id.textViewCategoryShowCards);
			textViewName.setText(name);
			textViewCategory.setText(category);
			buttonEdit = (Button) convertView.findViewById(R.id.btnEditShowCards);
			if (!creator.equals(fireBase.getUserEmail())) {
				buttonEdit.setVisibility(View.GONE);
			}
			buttonEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.edit(id, idRoom);
				}
			});
		}
	}
}
