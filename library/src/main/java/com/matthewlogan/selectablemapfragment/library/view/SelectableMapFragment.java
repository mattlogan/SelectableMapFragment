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

    public static final float sBaseOverlaySquareSize = 10000;

    private View mContentView;
    private GoogleMap mGoogleMap;

    private GroundOverlayOptions mDraggableOverlayBoxOptions;
    private GroundOverlay mDraggableOverlayBox;

    private OnOverlayDragListener mListener;

    private boolean mIsDraggingBox;

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

        if (event.getPointerCount() == 1) {

            final int w = getView().getWidth();
            final int h = getView().getHeight();

            LatLng touchPoint = OverlayUtils.convertPointToLatLng(w, h,
                    event.getX(), h - event.getY(), mGoogleMap);

            switch (event.getActionMasked()) {

                case MotionEvent.ACTION_DOWN:
                    if (OverlayUtils.isPointContainedInOverlay(mDraggableOverlayBox, touchPoint)) {
                        mIsDraggingBox = true;
                        mGoogleMap.getUiSettings().setScrollGesturesEnabled(Boolean.FALSE);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mIsDraggingBox && event.getHistorySize() > 0) {
                        LatLng lastTouchPoint = OverlayUtils.convertPointToLatLng(w, h,
                                event.getHistoricalX(0), h - event.getHistoricalY(0),
                                mGoogleMap);

                        LatLng oldPos = mDraggableOverlayBox.getPosition();

                        double diffLat = touchPoint.latitude - lastTouchPoint.latitude;
                        double diffLng = touchPoint.longitude - lastTouchPoint.longitude;

                        mDraggableOverlayBox.setPosition(new LatLng(
                                oldPos.latitude + diffLat,
                                oldPos.longitude + diffLng));

                        mListener.onOverlayDrag(mDraggableOverlayBox.getBounds());
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    mIsDraggingBox = false;
                    mGoogleMap.getUiSettings().setScrollGesturesEnabled(Boolean.TRUE);
                    break;

                default:
                    break;
            }

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
