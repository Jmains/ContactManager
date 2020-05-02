package com.example.contactmanager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapAddressActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;
    private LocationManager mLocManager;

    boolean mLocationPermissionGranted = false;
    private String mAddress = null;

    public double mAddressLatitude = 0.0;
    public double mAddressLongitude = 0.0;
    private double mCurrLat = 0.0;
    private double mCurrLong = 0.0;
    double mDistanceInMiles = 0;

    Snackbar mSnackBar;

    private static final String TAG = "MapAddressActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_address);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocationPermission();

        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mCurrLat = location.getLatitude();
                mCurrLong = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        Location location = mLocManager.getLastKnownLocation(mLocManager.GPS_PROVIDER);
        if (location != null) {
            mCurrLong = location.getLongitude();
            mCurrLat = location.getLatitude();
        }
        mSnackBar = Snackbar.make(mapFragment.getView(), "", Snackbar.LENGTH_INDEFINITE);

        // Create an intent to get the address from the calling activity
        Intent intent = getIntent();
        if (intent != null) {
            mAddress = intent.getStringExtra("fullAddress");
        }
        if (mAddress != null) {
            getLatitudeAndLongitude(mAddress);
        }
        // Calculate distance in meters between two coordinates
        float [] dist = new float[1];
        Location.distanceBetween(mCurrLat, mCurrLong, mAddressLatitude, mAddressLongitude, dist);
        // Convert to miles
        mDistanceInMiles = dist[0] * 0.00062137119;
    }

    // Get lat and long based on address name given using geocoder.
    // Method returns void and takes an address as a parameter
    private void getLatitudeAndLongitude(String addressString) {
        Geocoder geocoder = new Geocoder(MapAddressActivity.this);
        List<Address> geoAddresses = new ArrayList<>();
        try {
            geoAddresses = geocoder.getFromLocationName(addressString, 3);
        } catch (IOException e) {
            Log.d(TAG, "getGeoLocation: " + e.getMessage());
        }

        if (geoAddresses.size() > 0) {
            mAddressLatitude = geoAddresses.get(0).getLatitude();
            mAddressLongitude = geoAddresses.get(0).getLongitude();

        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateUI();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateUI();
    }

    // Update Snackbar with address, coordinates, and distance
    // Returns void and takes void as a parameter
    public void updateUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Grab address
                if (mAddress != null) {
                    DecimalFormat decimal = new DecimalFormat("#.#####");
                    // Add a marker in Address given and move the camera
                    LatLng address = new LatLng(mAddressLatitude, mAddressLongitude);
                    String latAndLong = "Latitude: " + decimal.format(mAddressLatitude) + "\nLongitude: " + decimal.format(mAddressLongitude);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(address).title(mAddress));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(address));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    marker.showInfoWindow();
                    // Display address, coordinates, and distance from current location
                    mSnackBar.setText(mAddress + " :: " + latAndLong + " :: " + "Dist: " + decimal.format(mDistanceInMiles) + " miles");
                    mSnackBar.show();
                }
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
