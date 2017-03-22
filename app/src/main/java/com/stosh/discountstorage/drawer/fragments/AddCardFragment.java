package com.stosh.discountstorage.drawer.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.stosh.discountstorage.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.zxing.BarcodeFormat.CODE_128;
import static com.google.zxing.BarcodeFormat.CODE_39;
import static com.google.zxing.BarcodeFormat.CODE_93;
import static com.google.zxing.BarcodeFormat.EAN_13;
import static com.google.zxing.BarcodeFormat.EAN_8;
import static com.google.zxing.BarcodeFormat.ITF;
import static com.google.zxing.BarcodeFormat.RSS_14;
import static com.google.zxing.BarcodeFormat.RSS_EXPANDED;
import static com.google.zxing.BarcodeFormat.UPC_A;
import static com.google.zxing.BarcodeFormat.UPC_E;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardFragment extends Fragment {

    private ImageView imageView;
    private TextView textViewCode;
    private Button buttonCancel;
    private Button buttonAdd;
    private Spinner spinner;
    private EditText editTextNameCard;
    private EditText editTextCategory;
    private View.OnClickListener onClickListener;
    private View view;

    private String format;
    private String code;
    private BitMatrix bitMatrix;
    private Bitmap bitmap;
    private BarcodeEncoder barcodeEncoder;

    private List roomList;
    private int spinnerPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_card, container, false);

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAdd:
                        String nameCard = editTextNameCard.getText().toString();
                        String category = editTextCategory.getText().toString();
                        if (TextUtils.isEmpty(nameCard)) {
                            return;
                        } else if (TextUtils.isEmpty(category)) {
                            return;
                        }
                        listener.onClickAddCard(roomList.get(spinnerPosition).toString(), nameCard, category, code, format);
                        Log.d("1", roomList.get(spinnerPosition).toString());
                    case R.id.btnCancel:
                        listener.onClickCancel();
                        break;
                }
            }
        };
        Bundle bundle = getArguments();
        code = bundle.getString("code");
        format = bundle.getString("format");
        roomList = (ArrayList) bundle.getStringArrayList("roomList");
        init();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, roomList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Title");
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
                Log.d("1", roomList.get(spinnerPosition).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSpinnerRoomsAdapter();
        try {

            switch (format) {
                case "UPC_A":
                    bitMatrix = new MultiFormatWriter().encode(code, UPC_A, 400, 200);
                    break;
                case "UPC_E":
                    bitMatrix = new MultiFormatWriter().encode(code, UPC_E, 400, 200);
                    break;
                case "EAN_8":
                    bitMatrix = new MultiFormatWriter().encode(code, EAN_8, 400, 200);
                    break;
                case "EAN_13":
                    bitMatrix = new MultiFormatWriter().encode(code, EAN_13, 400, 200);
                    break;
                case "CODE_39":
                    bitMatrix = new MultiFormatWriter().encode(code, CODE_39, 400, 200);
                    break;
                case "CODE_93":
                    bitMatrix = new MultiFormatWriter().encode(code, CODE_93, 400, 200);
                    break;
                case "CODE_128":
                    bitMatrix = new MultiFormatWriter().encode(code, CODE_128, 400, 200);
                    break;
                case "ITF":
                    bitMatrix = new MultiFormatWriter().encode(code, ITF, 400, 200);
                    break;
                case "RSS_14":
                    bitMatrix = new MultiFormatWriter().encode(code, RSS_14, 400, 200);
                    break;
                case "RSS_EXPANDED":
                    bitMatrix = new MultiFormatWriter().encode(code, RSS_EXPANDED, 400, 200);
                    break;
            }

            barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            textViewCode.setText(code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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


        buttonCancel.setOnClickListener(onClickListener);
        buttonAdd.setOnClickListener(onClickListener);
    }

    private void setSpinnerRoomsAdapter() {




    }

    private ListenerGenerate listener;

    public interface ListenerGenerate {
        public void onClickCancel();

        public void onClickAddCard(String nameRoom, String name, String category, String code, String format);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ListenerGenerate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements ListenerGenerate");
        }
    }


}
