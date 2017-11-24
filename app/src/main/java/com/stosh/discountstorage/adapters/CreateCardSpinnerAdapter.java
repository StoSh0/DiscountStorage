
package com.stosh.discountstorage.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stosh.discountstorage.R;

import java.util.ArrayList;


/**
 * Created by StoSh on 18-May-17.
 **/


public class CreateCardSpinnerAdapter extends ArrayAdapter {
	
	private Context context;
	private ArrayList<String> objects;
	private int recourse;
	
	public CreateCardSpinnerAdapter(Context context, int recourse,
									ArrayList<String> objects) {
		super(context, recourse, objects);
		this.context = context;
		this.objects = objects;
		this.recourse = recourse;
	}
	
	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(context, recourse, null);
		String id = objects.get(position);
		TextView name = (TextView) convertView.findViewById(R.id.textViewSpinnerCreateCard);
		name.setText(id);
		return convertView;
	}
	
	
}



