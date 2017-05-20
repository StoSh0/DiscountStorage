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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.adapters.ShowCardListAdapter;
import com.stosh.discountstorage.database.Card;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardListFragment extends Fragment implements ValueEventListener, AdapterView.OnItemClickListener {
	
	private TextView textView;
	private ListView listView;
	private ProgressBar progressBar;
	private DrawerFragmentListener drawerFragmentListener;
	private String roomId;
	private HashMap<String, Object> hm;
	private ArrayList<HashMap<String, Object>> cards;
	private ArrayList<String> cardList;
	private FireBaseSingleton fireBase;
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			drawerFragmentListener = (DrawerFragmentListener) context;
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
		fireBase = FireBaseSingleton.getInstance();
		roomId = getArguments().getString(Const.ID_CARD);
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
		cardList = new ArrayList<>();
		if (dataSnapshot.getValue() == null) {
			progressBar.setVisibility(View.GONE);
			textView.setText(getString(R.string.add_card_first));
			return;
		}
		for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
			
			RoomList roomList = roomsDataSnapshot.getValue(RoomList.class);
			cardList.add(roomList.ID);
		}
		cards = new ArrayList<>();
		final ValueEventListener listener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Card card = dataSnapshot.getValue(Card.class);
				Log.d("qwerty", dataSnapshot + "");
				if (card == null) {
					fireBase.deleteFromCardList(roomId, dataSnapshot.getKey());
					return;
				}
				hm = new HashMap<>();
				hm.put(Const.NAME, card.name);
				hm.put(Const.CAT, card.category);
				hm.put(Const.CREATOR, card.creator);
				hm.put(Const.ID_ROOM_LIST, roomId);
				hm.put(Const.ID, dataSnapshot.getKey());
				cards.add(hm);
				progressBar.setVisibility(View.GONE);
				listView.setAdapter(new ShowCardListAdapter(getActivity(), R.layout.list_item_show_cards, cards, drawerFragmentListener));
				listView.setOnItemClickListener(ShowCardListFragment.this);
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		};
		for (String id : cardList) {
			fireBase.getCard(id, listener);
		}
	}
	
	@Override
	public void onCancelled(DatabaseError databaseError) {
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String idItem = cardList.get(position);
		drawerFragmentListener.send(Const.ID_ROOM_LIST, idItem);
	}
}
