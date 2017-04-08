package com.stosh.discountstorage.fragments.drawer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowRoomFragment extends Fragment implements ValueEventListener {

    private ListView listView;
    private ProgressBar progressBar;
    private DrawerFragmentListener listener;
    private static final String NAME = "RoomName";
    private static final String ID = "ID";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DrawerFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement ListenerHand");
        }
    }

    public static ShowRoomFragment getInstance(@Nullable Bundle data) {
        ShowRoomFragment fragment = new ShowRoomFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
        fireBase.getRooms(this);
        View view = inflater.inflate(R.layout.fragment_show_room, container, false);
        listView = (ListView) view.findViewById(R.id.listViewRooms);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarShowRoom);
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
            hm.put(NAME, "Name: " + room.name);
            hm.put(ID, room.ID);
            roomList.add(hm);
        }
        boolean isEmpty = false;
        if(roomList.isEmpty()){
            hm = new HashMap<>();
            hm.put(NAME, getString(R.string.add_room_first));
            roomList.add(hm);
            isEmpty = true;
        }
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(), roomList,
                R.layout.my_list_item,
                new String[]{NAME, ID},
                new int[]{R.id.text1, R.id.text2}
        );
        progressBar.setVisibility(View.GONE);
        listView.setAdapter(adapter);
        if (isEmpty){
            return;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
                String IDItem = itemHashMap.get(ID).toString();
                Log.d("1" , IDItem);
                listener.sendList(IDItem);
            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
