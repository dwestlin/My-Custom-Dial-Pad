package com.example.dwest;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper db;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = new DatabaseHelper(this);
        rand = new Random();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Cursor data = db.getData();


        while(data.moveToNext()){
            if(!(data.getString(2).equals("???") || data.getString(3).equals("???"))){

                double latitude = Double.parseDouble(data.getString(2));
                double longitude = Double.parseDouble(data.getString(3));
                LatLng marker = new LatLng( 	latitude,  	longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(marker)
                        .title(data.getString(0))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                        .snippet(data.getString(1)));
            }
        }

        LatLng sweden = new LatLng( 	62.39081100,  	17.30692700);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sweden));
    }

}
