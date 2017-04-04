package com.stosh.discountstorage.fragments.drawer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.activities.DrawerActivity;
import com.stosh.discountstorage.database.RoomList;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardFragment extends Fragment implements ValueEventListener  {

    private View view;
    private ListView listView;

    public static ShowCardFragment getInstance(@Nullable Bundle data) {
        ShowCardFragment fragment = new ShowCardFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
        fireBase.getRooms(this);
        view = inflater.inflate(R.layout.fragment_show_card, container, false);
        listView = (ListView) view.findViewById(R.id.listViewRooms);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        List roomList = new ArrayList();

        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {

            RoomList room = roomsDataSnapshot.getValue(RoomList.class);
            roomList.add(room.ID);

            Log.d("1", "20");
        }

        ArrayAdapter adapter = new ArrayAdapter<>(
                getActivity(), R.layout.my_list_item,
                roomList);
        listView.setAdapter(adapter);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
