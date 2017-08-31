package com.jamesooi.mylocation;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
    implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    //private Location mLastLocation;

    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;


    private EditText mLatitude;
    private EditText mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("EXECUTION", "onCreate() executed");

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

        mLatitude = (EditText)findViewById(R.id.latitude);
        mLongitude = (EditText)findViewById(R.id.longitude);

        if(mGoogleApiClient == null) {
            mGoogleApiClient = getGoogleApiClient();

        }

        if(mLocationRequest == null) {
            mLocationRequest = createLocationRequest();
        }
    }

    private GoogleApiClient getGoogleApiClient() {
        Log.i("EXECUTION", "getGoogleApiClient() executed");
        return new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("EXECUTION", "onConnected() executed");

        /*mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
        }
        else {
            Log.e("EXECUTION", "mLastLocation is null");
        }*/
        startLocationUpdates();

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
    }

    private void updateUI() {
        mLatitude.setText(String.valueOf(mCurrentLocation.getLatitude()));
        mLongitude.setText(String.valueOf(mCurrentLocation.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOCATION", "Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e("LOCATION", "Failed");
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

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
