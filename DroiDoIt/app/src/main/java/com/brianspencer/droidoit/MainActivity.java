package com.brianspencer.droidoit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.TextView;
import android.bluetooth.*;
import android.net.wifi.*;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        final TextView textWiFi = (TextView)findViewById(R.id.textWiFi);
        ToggleButton toggleWiFi = (ToggleButton) findViewById(R.id.toggleWiFi);

        if(wifi != null) {
            if (wifi.isWifiEnabled()) {
                textWiFi.setText("WiFi is enabled");
                toggleWiFi.setChecked(true);
            } else {
                textWiFi.setText("WiFi is disabled");
                toggleWiFi.setChecked(false);
            }
        }
        else {
            textWiFi.setText("WiFi not supported");
            toggleWiFi.setEnabled(false);
        }


        toggleWiFi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    if(wifi != null) {
                        if (!wifi.isWifiEnabled())
                            wifi.setWifiEnabled(true);
                        textWiFi.setText("WiFi is enabled");
                    }
                    else
                        textWiFi.setText("WiFi not supported");
                } else {
                    // The toggle is disabled
                    if(wifi != null) {
                        if (wifi.isWifiEnabled())
                            wifi.setWifiEnabled(false);
                        textWiFi.setText("WiFi is disabled");
                    }
                    else
                        textWiFi.setText("WiFi not supported");
                }
            }
        });

        final  BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        final TextView textBT = (TextView)findViewById(R.id.textBluetooth);
        ToggleButton toggleBT = (ToggleButton) findViewById(R.id.toggleBluetooth);

        if(bt != null) {
            if(bt.isEnabled()) {
                textBT.setText("Bluetooth is enabled");
                toggleBT.setChecked(true);
            }
            else {
                textBT.setText("Bluetooth is disabled");
                toggleBT.setChecked(false);
            }
        }
        else {
            textBT.setText("Bluetooth not supported");
            toggleBT.setEnabled(false);
        }

        toggleBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                    if(bt == null)
                        textBT.setText("Bluetooth not supported");
                    else {
                        if(!bt.isEnabled()) {
                            bt.enable();
                            //Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            //startActivityForResult(enableBT, REQUEST_ENABLE_BT);
                        }
                        textBT.setText("Bluetooth is enabled");
                    }
                } else {
                    // The toggle is disabled
                    if(bt == null)
                        textBT.setText("Bluetooth not supported");
                    else {
                        if(bt.isEnabled()) {
                            bt.disable();
                        }
                        textBT.setText("Bluetooth is disabled");
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
