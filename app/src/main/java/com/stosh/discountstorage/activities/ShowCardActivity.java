package com.stosh.discountstorage.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowCardActivity extends AppCompatActivity implements ValueEventListener {
	
	private Unbinder unbinder;
	
	@BindView(R.id.imageViewShowCard)
	ImageView imageView;
	@BindView(R.id.textViewShowCardName)
	TextView textViewName;
	@BindView(R.id.textViewShowCardCode)
	TextView textViewCode;
	@BindView(R.id.progressBarShowCard)
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_card);
		unbinder = ButterKnife.bind(this);
		FireBaseSingleton fireBase = FireBaseSingleton.getInstance();
		String id = getIntent().getStringExtra(Const.ID);
		fireBase.getCard(id, this);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}
}
