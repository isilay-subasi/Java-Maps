package com.example.javamaps;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.javamaps.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedPreferences=MapsActivity.this.getSharedPreferences("com.example.javamaps",MODE_PRIVATE);
        registerLauncher();
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
        mMap.setOnMapClickListener(this::onMapLongClick);

        //enlem ve boylam , marker işareti sydneyden başlıyor
        //latitude -> enlem longitude -> boylam
        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //locationmanager - casting
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //System.out.println("location:"+location.toString());

                boolean info = sharedPreferences.getBoolean("info",false);
                if (!info){
                    LatLng userLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera((CameraUpdateFactory.newLatLngZoom(userLocation,15)));
                    sharedPreferences.edit().putBoolean("info",true).apply();
                }



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.getRoot(),"Permission needed for maps",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

                    }
                }).show();
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }


        }else{
            //İzin verilmişse direk konum güncellemlerini başlayabiliriz.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLaocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLaocation != null){
                LatLng lastUserLocation = new LatLng(lastLaocation.getLatitude(),lastLaocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
            }
            //Mavi imlecin yeri belli olması için
            mMap.setMyLocationEnabled(true);
        }


        //48.8582007,2.2937732
        // LatLng eiffel=new LatLng(48.8582007,2.2937732);
        // mMap.addMarker(new MarkerOptions().position(eiffel).title("Eiffel Tower"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(eiffel));
        //newLatLngZoom -> yakın olarak başlaması için bunu kullanabiliriz.
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,10));

    }
    private void registerLauncher(){
        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //permission granted
                    if(ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                        Location lastLaocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(lastLaocation != null){
                            LatLng lastUserLocation = new LatLng(lastLaocation.getLatitude(),lastLaocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15));
                        }
                    }
                }else{
                    //permission denied
                    Toast.makeText(MapsActivity.this, "Permission needed", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
    }
}