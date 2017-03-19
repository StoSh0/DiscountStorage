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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.SettingProfileActivity;
import com.stosh.discountstorage.database.RoomList;
import com.stosh.discountstorage.drawer.fragments.CreateRoomFragment;
import com.stosh.discountstorage.drawer.fragments.GenerateFragment;
import com.stosh.discountstorage.drawer.fragments.HandFragment;
import com.stosh.discountstorage.drawer.fragments.ScanFragment;


public class AllActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HandFragment.ListenerHand,
        CreateRoomFragment.ListenerCreateRoom {

    private String TAG = "Auth";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new ScanFragment();
        fragmentTransaction.replace(R.id.containerDrawer, fragment).commit();
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
                fragment = new HandFragment();
                fragmentTransaction.replace(R.id.containerDrawer, fragment).commit();
                break;
            case R.id.nav_show:
                break;
            case R.id.nav_create:
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragment = new CreateRoomFragment();
                fragmentTransaction.replace(R.id.containerDrawer, fragment).addToBackStack(null).commit();
                break;
            case R.id.nav_connect:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String TAG = "Scan";
        IntentResult result;
        Log.d(TAG, "3");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "4");
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
            } else {
                Log.d(TAG, "Scanned");
                String code = result.getContents();
                String format = result.getFormatName();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment fragment = new GenerateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("code", code);
                bundle.putString("format", format);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.containerDrawer, fragment).addToBackStack(null).commit();
            }
        }
    }

    @Override
    public void send(String code) {
        String format = "EAN_13";
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new GenerateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        bundle.putString("format", format);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.containerDrawer, fragment).addToBackStack(null).commit();
    }

    @Override
    public void createRoom(String name, String password) {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        String emailUserBD = (mUser.getEmail()).replace(".", "").toLowerCase();

        myRef = database.getReference("users");

        RoomList roomList =  new RoomList(name,password);


        myRef.child(emailUserBD).child("RoomList").child(name).setValue(roomList);

        /*Cards cards = new Cards("Rukavicka", "1", "4509584937548");

        myRef.child(emailUserDB).child("Rooms").child("Job").child("Rukavichka").setValue(cards);*/
    }
}
