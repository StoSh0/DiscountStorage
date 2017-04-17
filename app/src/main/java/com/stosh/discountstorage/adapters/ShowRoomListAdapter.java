package com.stosh.discountstorage.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.Const;

import java.util.HashMap;
import java.util.List;

/**
 * Created by StoSh on 16-Apr-17.
 */

public class ShowRoomListAdapter extends ArrayAdapter {

    private int layout;
    private List<HashMap<String, Object>> data;

    public ShowRoomListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HashMap<String, Object>> data) {
        super(context, resource, data);
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

    private class ViewHolder {
        private TextView textViewName;
        private TextView textViewSub;
        private Button buttonEdit;

        public void init(View convertView, final int position){
            HashMap<String, Object> itemHashMap = data.get(position);
            String name = itemHashMap.get(Const.NAME).toString();
            String creator = itemHashMap.get(Const.CREATOR).toString();

            textViewName = (TextView) convertView.findViewById(R.id.textViewNameShowRooms);
            textViewSub = (TextView) convertView.findViewById(R.id.textViewSubShowRooms);
            buttonEdit = (Button) convertView.findViewById(R.id.btnEditShowRooms);
            textViewName.setText(name);
            textViewSub.setText(creator);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "sddsd" + position, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}


