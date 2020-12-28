package com.example.cmprojeto;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    Bundle bundle;
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bundle= new Bundle();
        latitude=38.521741;
        longitude=-8.838514;
        //38.521741, -8.838514
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        getLocation();
        LatLng userLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener) v -> {
            bundle.putDouble("latitude", v.latitude);
            bundle.putDouble("longitude", v.longitude);
            Intent intent = new Intent(getApplicationContext(), NewSessionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });


    }
    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(MapsActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location !=null) {
                        Geocoder geocoder = new Geocoder(MapsActivity.this,
                                Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(MapsActivity.this
                    , new  String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }
}