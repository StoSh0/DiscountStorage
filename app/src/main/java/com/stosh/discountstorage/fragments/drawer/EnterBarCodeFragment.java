package com.stosh.discountstorage.fragments.drawer;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stosh.discountstorage.R;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterBarCodeFragment extends Fragment implements View.OnClickListener {


    private EditText editTextEnterHand;
    private Button buttonEnter;
    private View view;
    private DrawerFragmentListener listener;
    private InputMethodManager inputMethodManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DrawerFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement ListenerHand");
        }
    }

    public static EnterBarCodeFragment getInstance(@Nullable Bundle data) {
        EnterBarCodeFragment fragment = new EnterBarCodeFragment();
        fragment.setArguments(data == null ? new Bundle() : data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_drawer_enter_bar_code, container, false);
        editTextEnterHand = (EditText) view.findViewById(R.id.editTextEnter);
        buttonEnter = (Button) view.findViewById(R.id.btnEnter);
        buttonEnter.setOnClickListener(this);
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return view;
    }

    @Override
    public void onClick(View v) {
        String code = editTextEnterHand.getText().toString();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(getActivity(), getString(R.string.enter_barcode), Toast.LENGTH_SHORT).show();
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(buttonEnter.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        listener.send(code);
    }
}
