package com.stosh.discountstorage.drawer.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stosh.discountstorage.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterBarCodeFragment extends Fragment {


    private EditText editTextEnterHand;
    private Button buttonEnter;
    private View.OnClickListener onClickListener;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_enter_bar_code, container, false);
        final Activity activity = getActivity();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextEnterHand.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(activity, getString(R.string.enter_barcode), Toast.LENGTH_SHORT).show();
                    return;
                }

                listener.send(code);
            }
        };
        init();
        return view;
    }

    private void init() {
        editTextEnterHand = (EditText) view.findViewById(R.id.editTextEnter);
        buttonEnter = (Button) view.findViewById(R.id.btnEnter);
        buttonEnter.setOnClickListener(onClickListener);
    }

    private ListenerHand listener;

    public interface ListenerHand {
        public void send(String code);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            listener = (ListenerHand) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement ListenerHand");
        }
    }
}
