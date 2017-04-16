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

public class ShowCardListAdapter extends ArrayAdapter {

    private int layout;
    private List<HashMap<String, Object>> data;

    public ShowCardListAdapter(@NonNull Context context, @LayoutRes int resurse, @NonNull List<HashMap<String, Object>> data) {
        super(context, resurse, data);
        layout = resurse;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(layout, parent, false);
        viewHolder.init(convertView, position);
        return convertView;
    }

    private class ViewHolder {
        TextView textViewName;
        TextView textViewCategory;
        Button buttonEdit;

        public void init(View convertView, final int position) {
            HashMap<String, Object> hashMap = data.get(position);
            String name = hashMap.get(Const.NAME).toString();
            String category = hashMap.get(Const.CAT).toString();
            textViewName = (TextView) convertView.findViewById(R.id.textViewNameShowCards);
            textViewCategory = (TextView) convertView.findViewById(R.id.textViewCategoryShowCards);
            buttonEdit = (Button) convertView.findViewById(R.id.btnEditShowCards);
            textViewName.setText(name);
            textViewCategory.setText(category);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "sdsd" + position, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
