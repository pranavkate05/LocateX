package com.example.locatex.Fragment;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.location.LocationManager;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;
public class HomeFragment extends Fragment implements  OnMapReadyCallback{

    EditText etSearch;
    ImageView ivSearch;

    TextView tvLocation;
    TextView tvLocationStatus;

    Button btnExplore1, btnExplore2, btnExplore3, btnExplore4, btnExplore5, btnExplore6, btnExplore7;

    FusedLocationProviderClient fusedLocationProviderClient;

    ActivityResultLauncher<String[]> permissionLauncher;

    double latitude, longitude;
    GoogleMap mMap;

    @SuppressLint({"MissingInflatedId"})
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

        etSearch = view.findViewById(R.id.etHomeSearch);
        tvLocation = view.findViewById(R.id.tvHomeLocationName);
        tvLocationStatus = view.findViewById(R.id.tvLocationStatus);
        updateLocationStatus();
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
        ivSearch = view.findViewById(R.id.ivHomeSearch);
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

        btnExplore5 = view.findViewById(R.id.btnExplore5);
        btnExplore6 = view.findViewById(R.id.btnExplore6);
        btnExplore7 = view.findViewById(R.id.btnExplore7);

        btnExplore1.setOnClickListener(v -> loadNearbyPlaces("tourism", "attraction"));
        btnExplore2.setOnClickListener(v -> loadNearbyPlaces("tourism", "hotel"));
        btnExplore3.setOnClickListener(v -> loadNearbyPlaces("amenity", "restaurant"));
        btnExplore4.setOnClickListener(v -> loadNearbyPlaces("amenity", "cafe"));
        btnExplore5.setOnClickListener(v -> loadNearbyPlaces("shop", "supermarket"));
        btnExplore6.setOnClickListener(v -> loadNearbyPlaces("amenity", "hospital"));
        btnExplore7.setOnClickListener(v -> loadNearbyPlaces("amenity", "fuel"));




        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        permissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestMultiplePermissions(),
                        result -> getCurrentLocation());

        checkPermission();
        return view;

    }
    private void loadNearbyPlaces(String key, String value) {

        if (latitude == 0.0 || longitude == 0.0) {
            Toast.makeText(requireContext(), "Current location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mMap == null) return;

        // Clear existing markers but we'll re-add the current location
        mMap.clear();
        addCurrentLocationMarker();

        String query =
                "[out:json];" +
                        "node[" + key + "=" + value + "](around:5000," + latitude + "," + longitude + ");" +
                        "out body;";

        String url = "https://overpass-api.de/api/interpreter?data=" + Uri.encode(query);

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        mMap.clear();

                        JSONArray elements = response.getJSONArray("elements");

                        if (elements.length() == 0) {
                            Toast.makeText(requireContext(), "No nearby places found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int i = 0; i < elements.length(); i++) {
                            JSONObject obj = elements.getJSONObject(i);

                            double lat = obj.getDouble("lat");
                            double lon = obj.getDouble("lon");

                            String name = "Unknown Place";

                            if (obj.has("tags")) {
                                JSONObject tags = obj.getJSONObject("tags");
                                if (tags.has("name")) {
                                    name = tags.getString("name");
                                }
                            }

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(name));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error reading map data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show();
                });

        queue.add(request);
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


    private void addCurrentLocationMarker() {
        if (mMap != null && latitude != 0.0 && longitude != 0.0) {
            LatLng currentLatLng = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title("You are here"));
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

                        if (mMap != null) {
                            LatLng latLng = new LatLng(latitude, longitude);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            addCurrentLocationMarker();
                        }

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

    private void updateLocationStatus() {

        LocationManager locationManager =
                (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        boolean isLocationOn =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isLocationOn) {
            tvLocationStatus.setText("Location is ON");
        } else {
            tvLocationStatus.setText("Location is OFF");
            tvLocation.setText("Location unavailable");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocationStatus();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        if (latitude != 0.0 && longitude != 0.0) {
            LatLng latLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            addCurrentLocationMarker();
        }
        Toast.makeText(requireContext(), "Map Ready", Toast.LENGTH_SHORT).show();
    }
}
