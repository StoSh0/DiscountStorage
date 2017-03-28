package com.stosh.discountstorage.drawer.fragments;



import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class  ScanFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button buttonCancel;

    public static ScanFragment getInstance(@Nullable Bundle data) {
        ScanFragment fragment = new ScanFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        init();
        return view;
    }


    private void init() {
        buttonCancel = (Button) view.findViewById(R.id.btnScan);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }
}
