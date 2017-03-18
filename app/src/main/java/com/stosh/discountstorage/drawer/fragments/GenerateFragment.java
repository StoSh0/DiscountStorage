package com.stosh.discountstorage.drawer.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.stosh.discountstorage.R;

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
public class GenerateFragment extends Fragment {

    private ImageView imageView;
    private TextView textViewCode;
    private Button buttonCansel;
    private Button buttonAdd;
    private View view;
    private BitMatrix bitMatrix;
    private Bitmap bitmap;
    private BarcodeEncoder barcodeEncoder;
    private String format;
    private String code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_generate, container, false);


        Bundle bundle = getArguments();
        code = bundle.getString("code");
        format = bundle.getString("format");
        init();
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
        buttonCansel = (Button) view.findViewById(R.id.btnCancel);
        buttonAdd = (Button) view.findViewById(R.id.btnAdd);
        imageView = (ImageView) view.findViewById(R.id.imageViewBarcode);
        textViewCode = (TextView) view.findViewById(R.id.textViewCode);
    }


}
