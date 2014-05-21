package com.matthewlogan.selectablemapfragment.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.matthewlogan.selectablemapfragment.library.view.SelectableMapFragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;


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

        final TextView coordinatesTextView = (TextView) findViewById(R.id.coordinates_text_view);

        final NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(4);
        df.setMaximumFractionDigits(4);

        mMapFragment = new SelectableMapFragment();
        mMapFragment.setOnOverlayDragListener(new SelectableMapFragment.OnOverlayDragListener() {
            @Override
            public void onOverlayDrag(LatLngBounds latLngBounds) {
                coordinatesTextView.setText(
                        "SW: (" + df.format(latLngBounds.southwest.latitude) + " N, "
                        + df.format(latLngBounds.southwest.longitude) + " E)"
                        + "\n"
                        + "NE: (" + df.format(latLngBounds.northeast.latitude) + " N, "
                        + df.format(latLngBounds.northeast.longitude) + " E)"
                );
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
