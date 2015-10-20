package com.brianspencer.clayshootinglog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Map;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class RoundListActivity extends FragmentActivity
        implements RoundListFragment.Callbacks, RoundDetailFragment.DetailCallbacks {

    private static final String AD_UNIT_ID = "ca-app-pub-8117097765950820/1619806585";
    private AdView adView;
    private boolean mTwoPane;

    public RoundListActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(RoundContent.dataFile == null){
            try{
                RoundContent.dataFile = this.getDir("com.brianspencer.clayshootinglog", MODE_PRIVATE);
                RoundContent.loadItems();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_list);

        //TelephonyManager tm =(TelephonyManager)getSystemService(android.content.Context.TELEPHONY_SERVICE);
        //String deviceid = tm.getDeviceId();

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);

        FrameLayout fl = (FrameLayout)findViewById(R.id.frame_ad);
        fl.addView(adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("99000343109052")
                .build();

        adView.loadAd(adRequest);


        if (findViewById(R.id.round_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((RoundListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.round_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_add:
                openAdd();
                return true;
            //case R.id.action_settings:
                //openSettings();
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAdd(){
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            RoundDetailFragment fragment = new RoundDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.round_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, RoundDetailActivity.class);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onDateSelected(Date date) {
        Toast.makeText(getApplicationContext(), "Date Clicked - List Activity", Toast.LENGTH_LONG).show();
    }

    /**
     * Callback method from {@link RoundListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RoundDetailFragment.ARG_ITEM_ID, id);
            RoundDetailFragment fragment = new RoundDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.round_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, RoundDetailActivity.class);
            detailIntent.putExtra(RoundDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onDataUpdate(){

        RoundListFragment rlf = ((RoundListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.round_list));
        rlf.aa.notifyDataSetChanged();
    }


}
