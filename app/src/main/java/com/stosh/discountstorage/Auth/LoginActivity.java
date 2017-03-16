package com.stosh.discountstorage.Auth;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.stosh.discountstorage.R;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnClickBtnListener {

    private final int SING_UP = 2;
    private final int RESET = 3;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentTransaction = null;

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragment = new LoginFragment();
        fragmentTransaction.replace(R.id.containerLogin, fragment).commit();
    }

    @Override
    public void onClickBtn(int code) {
        switch (code){
            case SING_UP:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new SingUpFragment();
                fragmentTransaction.replace(R.id.containerLogin, fragment).commit();
                break;
            case RESET:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new PasswordResetFragment();
                fragmentTransaction.replace(R.id.containerLogin, fragment).addToBackStack(null).commit();
                break;
        }

    }

    @Override
    public void onLogin(String email, String password) {
        Toast.makeText(this, email + " " + password, Toast.LENGTH_SHORT).show();
    }
}
