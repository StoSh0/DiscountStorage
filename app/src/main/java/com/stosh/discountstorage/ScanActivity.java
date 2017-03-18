package com.stosh.discountstorage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class ScanActivity extends AppCompatActivity {

    private String TAG = "SCAN";

    private IntentResult result;
    private ImageView imageView;
    private Button button;
    private Button butto1;
    private Button butto2;
    private BitMatrix bitMatrix;
    private Bitmap bitmap;
    private BarcodeEncoder barcodeEncoder;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scan);
init();
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
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
                finish();
            } else {
                Log.d(TAG, "Scanned");

                try {
                    bitMatrix = new MultiFormatWriter().encode(result.getContents(), BarcodeFormat.EAN_13, 400,200);
                    barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                imageView.setVisibility(View.VISIBLE);
                butto1.setVisibility(View.VISIBLE);
                butto2.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void init(){
        button = (Button) findViewById(R.id.btnScan);
        butto1 = (Button) findViewById(R.id.btnCancel);
        butto2 = (Button) findViewById(R.id.btnAdd);
        imageView = (ImageView) findViewById(R.id.imageViewBarcode);
    }
}