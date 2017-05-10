package com.stosh.discountstorage.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stosh.discountstorage.FireBaseSingleton;
import com.stosh.discountstorage.R;
import com.stosh.discountstorage.fragments.drawer.ShowCardListFragment;
import com.stosh.discountstorage.fragments.drawer.ShowRoomListFragment;
import com.stosh.discountstorage.interfaces.Const;
import com.stosh.discountstorage.interfaces.DrawerFragmentListener;
import com.stosh.discountstorage.fragments.drawer.AddRoomFragment;
import com.stosh.discountstorage.fragments.drawer.CreateRoomFragment;
import com.stosh.discountstorage.fragments.drawer.EnterBarCodeFragment;


public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, DrawerFragmentListener {

    private FireBaseSingleton fireBase;
    private FragmentManager mFragmentManager;
    private Intent createCardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView textViewProfile = (TextView) navigationView
                .getHeaderView(0)
                .findViewById(R.id.textViewProfile);
        fireBase = FireBaseSingleton.getInstance();
        textViewProfile.setText(fireBase.getUserEmail());

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .setCustomAnimations
                        (R.anim.fragment_drawer_fade_in, R.anim.fragment_drawer_fade_out)
                .replace(R.id.containerDrawer, ShowRoomListFragment.getInstance(null))
                .commit();
        setTitle("Show all roms");
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
                getRequestedOrientation();
                Intent intent = new Intent(this, SettingProfileActivity.class);
                intent.putExtra("request", getRequestedOrientation());
                startActivity(new Intent(this, SettingProfileActivity.class));
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
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("Scan")
                        .setBeepEnabled(true)
                        .setBarcodeImageEnabled(true)
                        .initiateScan();
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
                        .replace(R.id.containerDrawer, ShowRoomListFragment.getInstance(null))
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
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.exit:
                FireBaseSingleton firebase = FireBaseSingleton.getInstance();
                firebase.singOut();
                startActivity(new Intent(DrawerActivity.this, MainActivity.class));
                finish();
        }
        setTitle(item.getTitle());
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
                createCardIntent = new Intent(this, CreateCardActivity.class);
                createCardIntent.putExtra(Const.CODE, code);
                createCardIntent.putExtra(Const.FORMAT, format);
                startActivity(createCardIntent);
            }
        }
    }

    private void startShowCardListFragment(Bundle bundle) {
        mFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.anim.fragment_drawer_list_fade_in,
                        R.anim.fragment_drawer_list_fade_out,
                        R.anim.fragment_drawer_list_fade_pop_in,
                        R.anim.fragment_drawer_list_fade_pop_out
                )
                .replace(R.id.containerDrawer, ShowCardListFragment.getInstance(bundle))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void send(String id, String code) {
        switch (id) {
            case Const.ID_ENTER:
                if (code.length() == 13) {
                    String format = "EAN_13";
                    createCardIntent = new Intent(this, CreateCardActivity.class);
                    createCardIntent.putExtra(Const.CODE, code);
                    createCardIntent.putExtra(Const.FORMAT, format);
                    startActivity(createCardIntent);
                } else {
                    Toast.makeText(this, getString(R.string.only_ean_13), Toast.LENGTH_LONG).show();
                }
                break;
            case Const.ID_CARD_LIST:
                Bundle bundle = new Bundle();
                bundle.putString(Const.ID, code);
                startShowCardListFragment(bundle);
                break;
            case Const.ID_ROOM_LIST:
                Intent intent = new Intent(this, ShowCardActivity.class);
                intent.putExtra(Const.ID, code);
                startActivity(intent);
                break;
        }
    }
}
