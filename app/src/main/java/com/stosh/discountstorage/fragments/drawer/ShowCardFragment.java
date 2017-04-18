package com.stosh.discountstorage.fragments.drawer;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.database.Card;
import com.stosh.discountstorage.interfaces.Const;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowCardFragment extends Fragment implements ValueEventListener{

    private ImageView imageView;
    private TextView textViewName, textViewCode;
    private ProgressBar progressBar;
    private FireBaseSingleton fireBase;
    private String id;

    public static ShowCardFragment getInstance(@Nullable Bundle data) {
        ShowCardFragment fragment = new ShowCardFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fireBase = FireBaseSingleton.getInstance();
        View view = inflater.inflate(R.layout.fragment_drawer_show_card, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageViewShowCard);
        textViewName = (TextView) view.findViewById(R.id.textViewShowCardName);
        textViewCode = (TextView) view.findViewById(R.id.textViewShowCardCode);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarShowCard);
        Bundle bundle = getArguments();
        id = bundle.getString(Const.ID);
        fireBase.getCard(id, this);
        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Card card = dataSnapshot.getValue(Card.class);
        String code = card.code;
        String format = card.format;
        String name = card.name;
        progressBar.setVisibility(View.GONE);
        textViewName.setVisibility(View.VISIBLE);
        textViewCode.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        textViewName.setText(name);
        textViewCode.setText(code);
        Bitmap bitmap;
        BarcodeEncoder barcodeEncoder;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.valueOf(format), 1000, 500);
            barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
