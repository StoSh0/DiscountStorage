package com.stosh.discountstorage.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CreateCardActivity extends AppCompatActivity implements ValueEventListener{

    private InputMethodManager inputMethodManager;
    private ArrayAdapter<String> adapter;
    private String ID, format, code;
    private FireBaseSingleton fireBase;
    private Unbinder unbinder;

    @BindView(R.id.imageViewCreateCard)
    ImageView imageView;
    @BindView(R.id.editTextCreateCardName)
    EditText editTextNameCard;
    @BindView(R.id.editTextCreateCardCategory)
    EditText editTextCategory;
    @BindView(R.id.textViewCreateCard)
    TextView textViewCode;
    @BindView(R.id.spinnerCreateCard)
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        unbinder = ButterKnife.bind(this);
        fireBase = FireBaseSingleton.getInstance();
        code = getIntent().getStringExtra(Const.CODE);
        format = getIntent().getStringExtra(Const.FORMAT);
        fireBase.getRooms(this);
        generateBitmap();
        inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @OnClick({R.id.btnCreateCardAdd, R.id.btnCreateCardCancel})
    public void onButtonClick(Button button) {
        switch (button.getId()) {
            case R.id.btnCreateCardAdd:
                String nameCard = editTextNameCard.getText().toString();
                String category = editTextCategory.getText().toString();
                if (TextUtils.isEmpty(nameCard)) {
                    editTextNameCard.setError(getString(R.string.enter_name_card));
                    break;
                } else if (TextUtils.isEmpty(category)) {
                    editTextCategory.setError(getString(R.string.enter_category));
                    break;
                } else if (adapter.getItem(0).equals(Const.ROOM_LIST_IS_EMPTY)) {
                    Toast.makeText(this, getString(R.string.first_room), Toast.LENGTH_LONG).show();
                    break;
                } else {
                    inputMethodManager.hideSoftInputFromWindow(button.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    fireBase.createCardList(ID, nameCard, category);
                    fireBase.createCard(ID, nameCard, category, code, format);
                    Toast.makeText(this, getString(R.string.card_add), Toast.LENGTH_LONG).show();
                    finish();
                    break;
                }
            case R.id.btnCreateCardCancel:
                finish();
                inputMethodManager.hideSoftInputFromWindow(button.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    private void generateBitmap() {
        Bitmap bitmap;
        BarcodeEncoder barcodeEncoder;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(code, BarcodeFormat.valueOf(format), 1000, 500);
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
        ArrayList<String> roomList = new ArrayList<>();
        final ArrayList<String> idList = new ArrayList<>();
        for (DataSnapshot roomsDataSnapshot : dataSnapshot.getChildren()) {
            RoomList room = roomsDataSnapshot.getValue(RoomList.class);
            roomList.add(room.name + " (" + room.creator+ ")");
            idList.add(room.ID);
        }
        if (roomList.isEmpty()) roomList.add(0, Const.ROOM_LIST_IS_EMPTY);

        adapter = new ArrayAdapter<>(this, R.layout.my_spiner_item, roomList);
        adapter.setDropDownViewResource(R.layout.my_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ID = idList.get(position);
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
        
            }
        });
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }
	
}
