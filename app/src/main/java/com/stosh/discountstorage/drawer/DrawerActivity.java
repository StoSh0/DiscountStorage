package com.stosh.discountstorage.drawer;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.drawer.fragments.AddRoomFragment;
import com.stosh.discountstorage.settings.SettingProfileActivity;
import com.stosh.discountstorage.drawer.fragments.AddCardFragment;
import com.stosh.discountstorage.drawer.fragments.CreateRoomFragment;
import com.stosh.discountstorage.drawer.fragments.EnterBarCodeFragment;
import com.stosh.discountstorage.drawer.fragments.ScanFragment;


public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        EnterBarCodeFragment.ListenerHand,
        CreateRoomFragment.ListenerCreateRoom {

    private String TAG = "Scan";
    private FragmentManager mFragmentManager;
    private Bundle bundle;

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

        mFragmentManager = getSupportFragmentManager();
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
                DrawerActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                mFragmentManager.beginTransaction()
                        .replace(R.id.containerDrawer, ScanFragment.getInstance(null))
                        .commit();
                break;
            case R.id.nav_hand:
                mFragmentManager.beginTransaction()
                        .replace(R.id.containerDrawer, EnterBarCodeFragment.getInstance(null))
                        .commit();
                break;
            case R.id.nav_show:
                break;
            case R.id.nav_create:
                mFragmentManager.beginTransaction()
                        .replace(R.id.containerDrawer, CreateRoomFragment.getInstance(null))
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_connect:
                mFragmentManager.beginTransaction()
                        .replace(R.id.containerDrawer, AddRoomFragment.getInstance(null))
                        .commit();
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
                bundle = new Bundle();
                bundle.putString("code", code);
                bundle.putString("format", format);
                startAddCardFragment();
            }
        }
    }

    @Override
    public void send(String code) {

        if (code.length() == 13){
            String format = "EAN_13";
            Bundle bundle = new Bundle();
            bundle.putString("code", code);
            bundle.putString("format", format);
            startAddCardFragment();
        }else {
            Toast.makeText(this, "Sorry, but now we scan only EAN-13", Toast.LENGTH_LONG).show();
        }
    }

    private void startAddCardFragment(){
        mFragmentManager.beginTransaction()
                .replace(R.id.containerDrawer, AddCardFragment.getInstance(bundle))
                .addToBackStack(null)
                .commit();
    }
}
