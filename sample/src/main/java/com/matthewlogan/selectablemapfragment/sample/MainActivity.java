package com.matthewlogan.selectablemapfragment.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.matthewlogan.selectablemapfragment.library.view.SelectableMapFragment;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "SelectableMapFragment-sample";

    private static LatLng sStartLocation = new LatLng(37.7833, -122.4167);
    private static float sStartZoom = 10.f;

    private SelectableMapFragment mMapFragment;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = new SelectableMapFragment();
        mMapFragment.setOnOverlayDragListener(new SelectableMapFragment.OnOverlayDragListener() {
            @Override
            public void onOverlayDrag(LatLngBounds latLngBounds) {
                Log.d(TAG, "onOverlayDrag: " + latLngBounds);
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, mMapFragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleMap == null) {
            mGoogleMap = mMapFragment.getMap();
            if (mGoogleMap != null) {
                moveCameraToStartLocation();
            }
        }
    }

    private void moveCameraToStartLocation() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sStartLocation, sStartZoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }
}