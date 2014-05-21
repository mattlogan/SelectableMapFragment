package com.matthewlogan.selectablemapfragment.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.matthewlogan.selectablemapfragment.library.R;

/**
 * Created by matthewlogan on 5/20/14.
 */

public class OverlayUtils {

    public static Bitmap makeSquareOverlayBitmap(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int minDimension = Math.min(dm.widthPixels, dm.heightPixels);
        int squareSize = Math.min(minDimension,
                (int) context.getResources().getDimension(R.dimen.max_overlay_square_size));

        Bitmap bm = Bitmap.createBitmap(squareSize, squareSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Drawable shape = context.getResources().getDrawable(R.drawable.square_overlay);
        shape.setBounds(0, 0, bm.getWidth(), bm.getHeight());
        shape.draw(canvas);

        return bm;
    }

    public static boolean isPointContainedInOverlay(GroundOverlay overlay, LatLng point) {
        BoundingBox bbox = BoundingBox.fromLatLngBounds(overlay.getBounds());
        return point.latitude > bbox.minLat && point.latitude < bbox.maxLat
                && point.longitude > bbox.minLng && point.longitude < bbox.maxLng;
    }

    public static LatLng convertPointToLatLng(float screenWidth, float screenHeight,
                                              float x, float y, GoogleMap googleMap) {
        float fractionX = x / screenWidth;
        float fractionY = y / screenHeight;

        LatLngBounds llb = googleMap.getProjection().getVisibleRegion().latLngBounds;
        BoundingBox bbox = BoundingBox.fromLatLngBounds(llb);

        double lat = bbox.minLat + fractionY * (bbox.maxLat - bbox.minLat);
        double lng = bbox.minLng + fractionX * (bbox.maxLng - bbox.minLng);

        return new LatLng(lat, lng);
    }
}