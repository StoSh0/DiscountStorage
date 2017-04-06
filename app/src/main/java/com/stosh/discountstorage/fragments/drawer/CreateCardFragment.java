package com.stosh.discountstorage.fragments.drawer;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.interfaces.Const;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCardFragment extends Fragment implements View.OnClickListener,
        ValueEventListener,
        AdapterView.OnItemSelectedListener {

    private ImageView imageView;
    private TextView textViewCode;
    private Button buttonCancel;
    private Button buttonAdd;
    private Spinner spinner;
    private EditText editTextNameCard;
    private EditText editTextCategory;
    private View view;


    private ArrayAdapter<String> adapter;
    private String roomName, format, code;
    private FireBaseSingleton fireBase;

    public static CreateCardFragment getInstance(@Nullable Bundle data) {
        CreateCardFragment fragment = new CreateCardFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_card, container, false);
        fireBase = FireBaseSingleton.getInstance();
        Bundle bundle = getArguments();
        code = bundle.getString("code");
        format = bundle.getString("format");
        init();
        fireBase.getRooms(this);
        generateBitmap();
        return view;
    }

    private void init() {
        buttonCancel = (Button) view.findViewById(R.id.btnCancel);
        buttonAdd = (Button) view.findViewById(R.id.btnAdd);
        imageView = (ImageView) view.findViewById(R.id.imageViewBarcode);
        textViewCode = (TextView) view.findViewById(R.id.textViewCode);
        spinner = (Spinner) view.findViewById(R.id.spinnerNameRoom);
        editTextNameCard = (EditText) view.findViewById(R.id.editTextNameCard);
        editTextCategory = (EditText) view.findViewById(R.id.editTextCategory);
        buttonCancel.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String nameCard = editTextNameCard.getText().toString();
                String category = editTextCategory.getText().toString();
                if (TextUtils.isEmpty(nameCard)) {
                    editTextNameCard.setError(getString(R.string.enter_name_card));
                    break;
                } else if (TextUtils.isEmpty(category)) {
                    editTextCategory.setError(getString(R.string.enter_category));
                    break;
                } else if (adapter.getItem(0).equals(Const.ROOM_LIST_IS_EMPTY)) {
                    Toast.makeText(getActivity(), getString(R.string.first_room), Toast.LENGTH_LONG).show();
                    break;
                } else {
                    fireBase.createCardList(roomName, nameCard, category);
                    fireBase.createCard(roomName, nameCard, category, code, format);
                    Toast.makeText(getActivity(), getString(R.string.card_add), Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                    break;
                }
            case R.id.btnCancel:
                getActivity().onBackPressed();
                break;
        }
    }

    private void generateBitmap() {
        Bitmap bitmap;
        BarcodeEncoder barcodeEncoder;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.valueOf(format), 600, 300);
            barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            textViewCode.setText(code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List roomList = new ArrayList();
        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
            RoomList room = roomsDataSnapshot.getValue(RoomList.class);
            roomList.add(room.ID);
        }
        if (roomList.isEmpty()) roomList.add(0, Const.ROOM_LIST_IS_EMPTY);

        adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                roomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        roomName = adapter.getItem(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
