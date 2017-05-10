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
import com.stosh.discountstorage.adapters.ShowCardListAdapter;
import com.stosh.discountstorage.database.CardList;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardListFragment extends Fragment implements ValueEventListener {

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

    public static ShowCardListFragment getInstance(@Nullable Bundle data) {
        ShowCardListFragment fragment = new ShowCardListFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
        Bundle bundle = getArguments();
        String roomId = bundle.getString(Const.NAME);
        fireBase.getCardList(roomId, this);
        View view = inflater.inflate(R.layout.fragment_drawer_show_card_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewCard);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarShowCardList);
        textView = (TextView) view.findViewById(R.id.textView_Show_Card_List);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final ArrayList<HashMap<String, Object>> cardList = new ArrayList<>();
        HashMap<String, Object> hm;
        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
            CardList card = roomsDataSnapshot.getValue(CardList.class);
            hm = new HashMap<>();
            hm.put(Const.NAME, "Name: " + card.name);
            hm.put(Const.CAT, "Category: " + card.category);
            hm.put(Const.ID, card.ID);
            cardList.add(hm);
        }
        progressBar.setVisibility(View.GONE);
        listView.setAdapter(new ShowCardListAdapter(getActivity(), R.layout.list_item_show_cards, cardList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> itemHashMap = cardList.get(position);
                String idItem = itemHashMap.get(Const.ID).toString();
                listener.send(Const.ID_ROOM_LIST, idItem);
            }
        });

        if(cardList.isEmpty()){
            textView.setText(getString(R.string.add_card_first));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

}
