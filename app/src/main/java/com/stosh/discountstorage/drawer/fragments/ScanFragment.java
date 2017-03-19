package com.stosh.discountstorage.drawer.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.stosh.discountstorage.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class  ScanFragment extends Fragment {

    private View view;
    private Button buttonCancel;
    private View.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        final Activity activity = getActivity();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        };
        init();
        return view;
    }


    private void init() {
        buttonCancel = (Button) view.findViewById(R.id.btnScan);
        buttonCancel.setOnClickListener(onClickListener);
    }
}
