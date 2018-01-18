package com.example.rick.programmeerproject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int DEFAULT_ZOOM = 14;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    int onsaved = 0;
    String locality = null;
    private CameraPosition mCameraPosition;
    private GoogleMap mMap;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private ArrayList names;
    private ArrayList lat;
    private ArrayList lon;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            names = intent.getStringArrayListExtra("name");
            lon = intent.getStringArrayListExtra("lon");
            lat = intent.getStringArrayListExtra("lat");
            setMarker();
        }
    };

    public void setMarker() {
        Context context = getApplicationContext();

        if (names.contains("No Location Found") && onsaved == 0) {
            Toast.makeText(context, "No breweries found in your locality", Toast.LENGTH_LONG)
                    .show();
        } else if (!names.contains("No Location Found") || onsaved != 1) {
            for (int i = 0; i < names.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(String
                        .valueOf(lat.get(i))), Double.valueOf(String.valueOf(lon.get(i))))).title
                        (names.get(i).toString()));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            onsaved = 1;
            names = savedInstanceState.getStringArrayList("names");
            lon = savedInstanceState.getStringArrayList("lon");
            lat = savedInstanceState.getStringArrayList("lat");
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_main);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setupNeeded();
    }

    protected void setupNeeded() {
        // Build the map.
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            outState.putStringArrayList("names", names);
            outState.putStringArrayList("lon", lon);
            outState.putStringArrayList("lat", lat);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        //        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
        //
        //            @Override
        //            // Return null here, so that getInfoContents() is called next.
        //            public View getInfoWindow(Marker arg0) {
        //                return null;
        //            }
        //
        //            @Override
        //            public View getInfoContents(Marker marker) {
        //                // Inflate the layouts for the info window, title and snippet.
        //                View infoWindow = getLayoutInflater().inflate(R.layout
        // .custom_info_contents,
        //                        (FrameLayout) findViewById(R.id.map), false);
        //
        //                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
        //                title.setText(marker.getTitle());
        //
        //                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
        //                snippet.setText(marker.getSnippet());
        //
        //                return infoWindow;
        //            }
        //        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        if (onsaved == 1) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(mCameraPosition);
            mMap.moveCamera(update);
            setMarker();
        } else {
            // Get the current location of the device and set the position of the map.
            control();
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng
                                    (mLastKnownLocation.getLatitude(), mLastKnownLocation
                                            .getLongitude()), DEFAULT_ZOOM));
                            getCity();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest
                .permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                    .ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }
            }
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void control() {
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS enabled
            getDeviceLocation();
        } else {
            //GPS disabled
            Toast.makeText(this, "Please turn on location services", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
        return true;
    }

    public void getCity() {
        double lat;
        double lon;
        lat = mLastKnownLocation.getLatitude();
        lon = mLastKnownLocation.getLongitude();
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            locality = addresses.get(0).getLocality();
            citySearch();
        }
    }

    public void citySearch() {
        CityAsyncTask asyncTask = new CityAsyncTask(this);
        asyncTask.execute(locality);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter
                ("breweries"));
    }
}
