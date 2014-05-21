package com.matthewlogan.selectablemapfragment.library.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.matthewlogan.selectablemapfragment.library.util.OverlayUtils;

/**
 * Created by matthewlogan on 5/20/14.
 */

public class SelectableMapFragment extends SupportMapFragment
        implements TouchableMapWrapper.OnMapTouchListener, GoogleMap.OnMapClickListener {

    private View mContentView;
    private GoogleMap mGoogleMap;

    private GroundOverlayOptions mDraggableOverlayBoxOptions;
    private GroundOverlay mDraggableOverlayBox;

    // Starting size for map creation
    public static final float sBaseOverlaySquareSize = 10000;

    private OnOverlayDragListener mListener;

    public interface OnOverlayDragListener {
        public void onOverlayDrag(LatLngBounds latLngBounds);
    }

    public SelectableMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mContentView = super.onCreateView(inflater, parent, savedInstanceState);

        TouchableMapWrapper touchableMapWrapper = new TouchableMapWrapper(getActivity());
        touchableMapWrapper.addView(mContentView);
        touchableMapWrapper.setOnMapTouchListener(this);

        return touchableMapWrapper;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleMap == null) {
            mGoogleMap = getMap();
            if (mGoogleMap != null) {
                mGoogleMap.setOnMapClickListener(this);
                showSelectionBox();
            }
        }
    }

    @Override
    public View getView() {
        return mContentView;
    }

    @Override
    public void onMapTouch(MotionEvent event) {
        LatLng ll = OverlayUtils.convertPointToLatLng(getView().getWidth(),
                getView().getHeight(), event.getX(),
                getView().getHeight() - event.getY(), mGoogleMap);

        if (event.getPointerCount() == 1
                && OverlayUtils.isPointContainedInOverlay(mDraggableOverlayBox, ll)) {
            mDraggableOverlayBox.setPosition(ll);
            mListener.onOverlayDrag(mDraggableOverlayBox.getBounds());
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(Boolean.FALSE);
        } else {
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(Boolean.TRUE);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mDraggableOverlayBox.setPosition(latLng);
        mListener.onOverlayDrag(mDraggableOverlayBox.getBounds());
    }

    public void setOnOverlayDragListener(OnOverlayDragListener listener) {
        mListener = listener;
    }

    private void showSelectionBox() {
        if (mDraggableOverlayBoxOptions == null) {
            Bitmap bm = OverlayUtils.makeSquareOverlayBitmap(getActivity());
            LatLng startPosition = mGoogleMap.getCameraPosition().target;

            mDraggableOverlayBoxOptions = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromBitmap(bm))
                    .zIndex(1)
                    .position(startPosition, sBaseOverlaySquareSize);
        }

        mDraggableOverlayBox = mGoogleMap.addGroundOverlay(mDraggableOverlayBoxOptions);
    }
}
