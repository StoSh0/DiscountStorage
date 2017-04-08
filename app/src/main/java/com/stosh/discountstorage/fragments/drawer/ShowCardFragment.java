package com.stosh.discountstorage.fragments.drawer;


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
import com.stosh.discountstorage.database.CardList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardFragment extends Fragment implements ValueEventListener {

    private ListView listView;
    private ProgressBar progressBar;
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
        View view = inflater.inflate(R.layout.fragment_show_card, container, false);
        listView = (ListView) view.findViewById(R.id.listViewCard);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarShowCard);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<HashMap<String, Object>> cardList = new ArrayList<>();
        HashMap<String, Object> hm;
        Log.d("1", "2" + dataSnapshot.getChildren());
        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
            CardList card = roomsDataSnapshot.getValue(CardList.class);
            hm = new HashMap<>();
            hm.put(NAME, "Name: " + card.name);
            hm.put(CAT, "Category: " + card.category);
            cardList.add(hm);
        }

        boolean isEmpty = false;
        if(cardList.isEmpty()){
            hm = new HashMap<>();
            hm.put(NAME, getString(R.string.add_room_first));
            cardList.add(hm);
            isEmpty = true;
        }
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(), cardList,
                R.layout.my_list_item,
                new String[]{NAME,CAT},
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
                String titleItem = itemHashMap.get(NAME).toString();
                String IDItem = itemHashMap.get(CAT).toString();
            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
