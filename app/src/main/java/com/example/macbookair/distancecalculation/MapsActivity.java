package com.example.macbookair.distancecalculation;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

/**
 * Run app and tap the screen anywhere to add 2 markers.
 * Please don't try to add markers more than two.
 * After adding them, app shows you distance between markers in km (using Toast).
 * THANKS.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int REQUEST_LOCATION_CODE = 1234;
    private LatLng position1;
    private LatLng position2;
    private Marker marker1, marker2;
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (count > 1) return; //Only two markers

                if (count == 0) {
                    position1 = latLng;
                    count++;
                } else if (count == 1) {
                    position2 = latLng;
                    count++;
                }

                if (position1 != null)
                    marker1 = mMap.addMarker(new MarkerOptions().position(position1));

                if (position2 != null)
                    marker2 = mMap.addMarker(new MarkerOptions().position(position2));

                if (position1 != null && position2 != null) {
                    mMap.addPolyline(new PolylineOptions().add(position1, position2).width(6));
                    double disanceD = SphericalUtil.computeDistanceBetween(position1, position2);
                    int distance = (int) disanceD / 1000;// show distance with KM
                    Toast.makeText(getApplicationContext(), "Approx. Distance: " + distance + "km", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            count = 0;
            marker1.remove();
            marker2.remove();
        }

        return true;
    }
}
