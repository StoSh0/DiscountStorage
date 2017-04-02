package com.stosh.discountstorage.fragments.drawer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stosh.discountstorage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardFragment extends Fragment {


    public static ShowCardFragment getInstance(@Nullable Bundle data) {
        ShowCardFragment fragment = new ShowCardFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_card, container, false);
    }

}
