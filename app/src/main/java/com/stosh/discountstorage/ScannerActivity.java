package com.stosh.discountstorage;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class ScannerActivity extends AppCompatActivity {

    private String TAG = "SCAN";

    Button button;

    BitMatrix bitMatrix;
    Bitmap bitmap;
    BarcodeEncoder barcodeEncoder;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);

        button = (Button) findViewById(R.id.button4);
        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
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
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
                finish();
            } else {
                Log.d(TAG, "Scanned");
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    bitMatrix = multiFormatWriter.encode(result.getContents(), BarcodeFormat.EAN_13, 200,200);
                    barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = new GenerateFragment();
                fragmentTransaction.replace(R.id.containerGenerate, fragment).commit();

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}