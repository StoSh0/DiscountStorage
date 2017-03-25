package com.stosh.discountstorage.drawer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.SettingProfileActivity;
import com.stosh.discountstorage.database.Room;
import com.stosh.discountstorage.drawer.fragments.AddCardFragment;
import com.stosh.discountstorage.drawer.fragments.CreateRoomFragment;
import com.stosh.discountstorage.drawer.fragments.EnterBarCodeFragment;
import com.stosh.discountstorage.drawer.fragments.ScanFragment;

import java.util.ArrayList;
import java.util.List;


public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        EnterBarCodeFragment.ListenerHand,
        CreateRoomFragment.ListenerCreateRoom {

    private final String OK_ID = "OK";

    private String TAG = "Scan";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser mUser;
    private String email;
    private String emailUserBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFireBase();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingProfileActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction;
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_camera:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new ScanFragment();
                fragmentTransaction.replace(R.id.containerDrawer, fragment).commit();
                break;
            case R.id.nav_hand:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new EnterBarCodeFragment();
                fragmentTransaction.replace(R.id.containerDrawer, fragment).commit();
                break;
            case R.id.nav_show:
                break;
            case R.id.nav_create:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new CreateRoomFragment();
                fragmentTransaction
                        .replace(R.id.containerDrawer, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_connect:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void responseCreateRoom() {
        Toast.makeText(this, getString(R.string.room_was_create), Toast.LENGTH_LONG).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result;
        super.onActivityResult(requestCode, resultCode, data);
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
            } else {
                Log.d(TAG, "Scanned");
                String code = result.getContents();
                String format = result.getFormatName();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = new AddCardFragment();
                Bundle bundle = new Bundle();
                bundle.putString("code", code);
                bundle.putString("format", format);
                fragment.setArguments(bundle);
                fragmentTransaction
                        .replace(R.id.containerDrawer, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void send(String code) {

        if (code.length() == 13){
            String format = "EAN_13";
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new AddCardFragment();
            Bundle bundle = new Bundle();
            bundle.putString("code", code);
            bundle.putString("format", format);
            fragment.setArguments(bundle);
            fragmentTransaction
                    .replace(R.id.containerDrawer, fragment)
                    .addToBackStack(null)
                    .commit();
        }else {
            Toast.makeText(this, "Sorry, but now we scan only EAN-13", Toast.LENGTH_LONG).show();
        }
    }

    private void initFireBase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mUser = mAuth.getCurrentUser();
        email = mUser.getEmail();
        emailUserBD = email.replace(".", "").toLowerCase();
        myRef = database.getReference("users");
    }
}
