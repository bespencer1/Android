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
import android.location.*;

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

        final TextView textLat = (TextView)findViewById(R.id.textLat);
        final TextView textLon = (TextView)findViewById(R.id.textLon);
        final TextView textSpeed = (TextView)findViewById(R.id.textSpeed);
        final TextView textAlt = (TextView)findViewById(R.id.textAlt);
        final TextView textGPS = (TextView)findViewById(R.id.textGPS);
        final ToggleButton toggleGPS = (ToggleButton) findViewById(R.id.toggleGPS);
        toggleGPS.setEnabled(false);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            textGPS.setText("GPS is enabled");
            toggleGPS.setChecked(true);
        }
        else {
            textGPS.setText("GPS is disabled");
            toggleGPS.setChecked(false);
        }

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                textLat.setText("Lat: " + String.valueOf(location.getLatitude()));
                textLon.setText("Lon: " + String.valueOf(location.getLongitude()));
                textSpeed.setText("Speed: " + String.valueOf(location.getSpeed()) + " meters/sec");
                textAlt.setText("Alt: " + String.valueOf(location.getAltitude()));

                textGPS.setText("GPS is enabled");
                toggleGPS.setChecked(true);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        TextView textWiFiAP = (TextView)findViewById(R.id.textWiFiAP);
        WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()){
            textWiFiAP.setText("Connected to: " + wifiManager.getConnectionInfo().getSSID());
        }
        else {
            textWiFiAP.setText("");
        }

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

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
