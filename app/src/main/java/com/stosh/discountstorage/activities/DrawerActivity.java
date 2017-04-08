package com.stosh.discountstorage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.stosh.discountstorage.fragments.drawer.ShowCardFragment;
import com.stosh.discountstorage.fragments.drawer.ShowRoomFragment;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;
import com.stosh.discountstorage.fragments.drawer.AddRoomFragment;
import com.stosh.discountstorage.fragments.drawer.CreateCardFragment;
import com.stosh.discountstorage.fragments.drawer.CreateRoomFragment;
import com.stosh.discountstorage.fragments.drawer.EnterBarCodeFragment;


public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, DrawerFragmentListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .setCustomAnimations
                        (R.anim.fragment_drawer_fade_in, R.anim.fragment_drawer_fade_out)
                .replace(R.id.containerDrawer, ShowRoomFragment.getInstance(null))
                .commit();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_camera:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                break;
            case R.id.nav_hand:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations
                                (R.anim.fragment_drawer_fade_in, R.anim.fragment_drawer_fade_out)
                        .replace(R.id.containerDrawer, EnterBarCodeFragment.getInstance(null))
                        .commit();
                break;
            case R.id.nav_show:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations
                                (R.anim.fragment_drawer_fade_in, R.anim.fragment_drawer_fade_out)
                        .replace(R.id.containerDrawer, ShowRoomFragment.getInstance(null))
                        .commit();
                break;
            case R.id.nav_create:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations
                                (R.anim.fragment_drawer_fade_in, R.anim.fragment_drawer_fade_out)
                        .replace(R.id.containerDrawer, CreateRoomFragment.getInstance(null))
                        .commit();
                break;
            case R.id.nav_connect:
                mFragmentManager.beginTransaction()
                        .setCustomAnimations
                                (R.anim.fragment_drawer_fade_in, R.anim.fragment_drawer_fade_out)
                        .replace(R.id.containerDrawer, AddRoomFragment.getInstance(null))
                        .commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result;
        super.onActivityResult(requestCode, resultCode, data);
        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String TAG = "Scan";
            if (result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
            } else {
                Log.d(TAG, "Scanned");
                String code = result.getContents();
                String format = result.getFormatName();
                Bundle bundle = new Bundle();
                bundle.putString("code", code);
                bundle.putString("format", format);
                startAddCardFragment(bundle);
            }
        }
    }

    @Override
    public void send(String code) {
        Log.d("1", code);
        if (code.length() == 13) {
            String format = "EAN_13";
            Bundle bundle = new Bundle();
            bundle.putString("code", code);
            bundle.putString("format", format);
            startAddCardFragment(bundle);
        } else {
            Toast.makeText(this, getString(R.string.only_ean_13), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendList(String roomName) {
        Bundle bundle = new Bundle();
        bundle.putString("roomName", roomName);
        startShowCardFragment(bundle);
    }

    private void startAddCardFragment(Bundle bundle) {
        mFragmentManager.beginTransaction()
                .replace(R.id.containerDrawer, CreateCardFragment.getInstance(bundle))
                .addToBackStack(null)
                .commit();
    }

    private void startShowCardFragment(Bundle bundle){
        mFragmentManager.beginTransaction()
                .replace(R.id.containerDrawer, ShowCardFragment.getInstance(bundle))
                .addToBackStack(null)
                .commit();
    }
}
