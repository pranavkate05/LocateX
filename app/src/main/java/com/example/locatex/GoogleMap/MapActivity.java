package com.example.locatex.GoogleMap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.locatex.R;
import com.example.locatex.databinding.ActivityLocationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.content.Intent;
import android.provider.Settings;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private GoogleMap mMap;
    private ActivityLocationBinding binding;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.fabLayers)
                .setOnClickListener(v -> showMapTypeDialog());
    }


    private void showMapTypeDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(this);

        View view = getLayoutInflater()
                .inflate(R.layout.bottom_map_type, null);

        LinearLayout defaultMap =
                view.findViewById(R.id.layoutDefault);

        LinearLayout satelliteMap =
                view.findViewById(R.id.layoutSatellite);

        LinearLayout terrainMap =
                view.findViewById(R.id.layoutTerrain);

        defaultMap.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
            dialog.dismiss();
        });

        satelliteMap.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
            dialog.dismiss();
        });

        terrainMap.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }
    private void showLocationOffDialog() {

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Device location")
                .setMessage("Please turn on your phone's Location ")
                .setCancelable(false)
                .setPositiveButton("Turn on", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mMap != null) {
            checkLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Location Access")
                .setMessage("Your live location will be displayed on the map.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    checkLocationPermission();
                })
                .show();
    }

    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST
            );

        } else {
            startLiveLocation();
        }
    }


    private void startLiveLocation() {

        locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            showLocationOffDialog();
            return;
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                LatLng userLocation = new LatLng(
                        location.getLatitude(),
                        location.getLongitude()
                );

                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                userLocation,
                                18f
                        )
                );
            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1,
                locationListener
        );

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                1,
                locationListener
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == LOCATION_PERMISSION_REQUEST) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startLiveLocation();

            } else {

                Toast.makeText(
                        this,
                        "Location permission denied",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}