package com.example.locatex.Fragment;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.locatex.HomepageActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.example.locatex.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements  OnMapReadyCallback{

    EditText etSearch;
    ImageView ivSearch;

    TextView tvLocation;

    Button btnExplore1, btnExplore2, btnExplore3, btnExplore4, btnExplore5;

    FusedLocationProviderClient fusedLocationProviderClient;

    ActivityResultLauncher<String[]> permissionLauncher;

    double latitude, longitude;
    GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            Toast.makeText(requireContext(), "Map Fragment is NULL", Toast.LENGTH_LONG).show();
        } else {
            mapFragment.getMapAsync(this);
        }

        etSearch = view.findViewById(R.id.etSearch);
        tvLocation = view.findViewById(R.id.Location);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {

            String location = etSearch.getText().toString().trim();

            if (!location.isEmpty()) {

                Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(location));

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }

            return true;
        });
        ivSearch = view.findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(v -> {
            if (mMap == null) {
                Toast.makeText(requireContext(), "Map is not ready", Toast.LENGTH_SHORT).show();
                return;
            }

            String location = etSearch.getText().toString().trim();

            if (location.isEmpty()) {
                Toast.makeText(requireContext(), "Enter location", Toast.LENGTH_SHORT).show();
                return;
            }

            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

            try {

                List<Address> addresses = geocoder.getFromLocationName(location, 1);

                if (addresses != null && !addresses.isEmpty()) {

                    Address address = addresses.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.clear();

                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(location));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                } else {

                    Toast.makeText(requireContext(),
                            "Location not found",
                            Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        btnExplore1 = view.findViewById(R.id.btnExplore1);
        btnExplore2 = view.findViewById(R.id.btnExplore2);
        btnExplore3 = view.findViewById(R.id.btnExplore3);
        btnExplore4 = view.findViewById(R.id.btnExplore4);
        btnExplore5 = view.findViewById(R.id.btnExplore5);


        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        permissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> getCurrentLocation());

        checkPermission();
        return view;

    }

    private void openMapSearch(String place) {

        String query = place + " near me";

        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(query));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            startActivity(intent);
        }
    }


    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();

        } else {

            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });

        }

    }


    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        Geocoder geocoder =
                                new Geocoder(requireContext(), Locale.getDefault());

                        try {

                            List<Address> addresses =
                                    geocoder.getFromLocation(latitude, longitude, 1);

                            if (addresses != null && !addresses.isEmpty()) {

                                tvLocation.setText(addresses.get(0).getAddressLine(0));

                            }

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    } else {

                        Toast.makeText(requireContext(),
                                "Location not found",
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
       mMap = googleMap;
        Toast.makeText(requireContext(), "Map Ready", Toast.LENGTH_SHORT).show();
    }
}