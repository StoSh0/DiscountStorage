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

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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


public class ScanActivity extends AppCompatActivity {

    private String TAG = "SCAN";

    private IntentResult result;

    private BitMatrix bitMatrix;
    private Bitmap bitmap;
    private BarcodeEncoder barcodeEncoder;

    private String code;
    private String format;

    private ImageView imageView;
    private Button button;
    private Button butto1;
    private Button butto2;


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
                generate();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void generate(){
        code = result.getContents();
        format = result.getFormatName();

        try {

            switch (format){
                case "UPC_A":
                    bitMatrix = new MultiFormatWriter().encode(code, UPC_A, 400,200);
                    break;
                case "UPC_E":
                    bitMatrix = new MultiFormatWriter().encode(code, UPC_E, 400,200);
                    break;
                case "EAN_8":
                    bitMatrix = new MultiFormatWriter().encode(code, EAN_8, 400,200);
                    break;
                case "EAN_13":
                    bitMatrix = new MultiFormatWriter().encode(code, EAN_13, 400,200);
                    break;
                case "CODE_39":
                    bitMatrix = new MultiFormatWriter().encode(code, CODE_39, 400,200);
                    break;
                case "CODE_93":
                    bitMatrix = new MultiFormatWriter().encode(code, CODE_93, 400,200);
                    break;
                case "CODE_128":
                    bitMatrix = new MultiFormatWriter().encode(code, CODE_128, 400,200);
                        break;
                case "ITF":
                    bitMatrix = new MultiFormatWriter().encode(code, ITF, 400,200);
                    break;
                case "RSS_14":
                    bitMatrix = new MultiFormatWriter().encode(code, RSS_14, 400,200);
                    break;
                case "RSS_EXPANDED":
                    bitMatrix = new MultiFormatWriter().encode(code, RSS_EXPANDED, 400,200);
                    break;
            }

            barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        imageView.setVisibility(View.VISIBLE);
        butto1.setVisibility(View.VISIBLE);
        butto1.setText(code);
        butto2.setText(format);
        butto2.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
    }
    private void init(){
        button = (Button) findViewById(R.id.btnScan);
        butto1 = (Button) findViewById(R.id.btnCancel);
        butto2 = (Button) findViewById(R.id.btnAdd);
        imageView = (ImageView) findViewById(R.id.imageViewBarcode);
    }
}