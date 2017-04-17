package com.stosh.discountstorage.fragments.drawer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.adapters.ShowRoomListAdapter;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRoomListFragment extends Fragment implements ValueEventListener {

    private TextView textView;
    private ListView listView;
    private ProgressBar progressBar;
    private DrawerFragmentListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DrawerFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement ListenerHand");
        }
    }

    public static ShowRoomListFragment getInstance(@Nullable Bundle data) {
        ShowRoomListFragment fragment = new ShowRoomListFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
        fireBase.getRooms(this);
        View view = inflater.inflate(R.layout.fragment_show_room_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewRooms);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarShowRoom);
        textView = (TextView) view.findViewById(R.id.textView_Show_Room_List);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<HashMap<String, Object>> roomList = new ArrayList<>();
        HashMap<String, Object> hm;
        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
            RoomList room = roomsDataSnapshot.getValue(RoomList.class);
            hm = new HashMap<>();
            hm.put(Const.NAME, room.name);
            hm.put(Const.CREATOR, room.creator);
            hm.put(Const.ID, room.ID);
            roomList.add(hm);
        }
        progressBar.setVisibility(View.GONE);
        listView.setAdapter(new ShowRoomListAdapter(getActivity(), R.layout.list_item_show_rooms, roomList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
                String IDItem = itemHashMap.get(Const.ID).toString();
                listener.sendList(IDItem);
            }
        });
        if(roomList.isEmpty()){
            textView.setText(getString(R.string.add_room_first));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
