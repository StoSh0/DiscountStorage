package com.stosh.discountstorage.fragments.drawer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.database.Card;
import com.stosh.discountstorage.database.CardList;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.interfaces.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardFragment extends Fragment implements ValueEventListener {

    private View view;
    private ListView listView;
    private ArrayList<HashMap<String, Object>> cardList;
    private static final String NAME = "CardName";
    private static final String CAT = "Category";

    public static ShowCardFragment getInstance(@Nullable Bundle data) {
        ShowCardFragment fragment = new ShowCardFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
        Bundle bundle = getArguments();
        String roomName = bundle.getString("roomName");
        fireBase.getCards(roomName, this);
        view = inflater.inflate(R.layout.fragment_show_card, container, false);
        listView = (ListView) view.findViewById(R.id.listViewCard);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("1", "1");
        cardList = new ArrayList<>();
        HashMap<String, Object> hm;
        Log.d("1", "2" + dataSnapshot.getChildren());
        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
            CardList card = roomsDataSnapshot.getValue(CardList.class);
            hm = new HashMap<>();
            hm.put(NAME, "Name: " + card.name);
            hm.put(CAT, "Category: " + card.category);
            cardList.add(hm);
            Log.d("1", "3");
        }
        Log.d("1", "4");
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(), cardList,
                R.layout.my_list_item,
                new String[]{NAME,CAT},
                new int[]{R.id.text1, R.id.text2}
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
                String titleItem = itemHashMap.get(NAME).toString();
                String IDItem = itemHashMap.get(CAT).toString();
            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
Log.d("1", "sdsaad");
    }

}
