package com.example.javamaps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.javamaps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        //enlem ve boylam , marker işareti sydneyden başlıyor
        //latitude -> enlem longitude -> boylam
        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //locationmanager - casting
        LocationManager locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                System.out.println("location:"+location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };


        //48.8582007,2.2937732
        LatLng eiffel=new LatLng(48.8582007,2.2937732);
        mMap.addMarker(new MarkerOptions().position(eiffel).title("Eiffel Tower"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(eiffel));
        //newLatLngZoom -> yakın olarak başlaması için bunu kullanabiliriz.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,10));

    }
}