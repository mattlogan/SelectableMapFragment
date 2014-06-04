package com.matthewlogan.selectablemapfragment.library;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;

/**
 * Created by matthewlogan on 5/20/14.
 */

public class BoundingBox implements Serializable {

    public double minLng;
    public double minLat;
    public double maxLng;
    public double maxLat;

    public BoundingBox() {
    }

    public static BoundingBox fromLatLngBounds(LatLngBounds bounds) {
        BoundingBox bbox = new BoundingBox();
        bbox.minLng = Math.min(bounds.southwest.longitude, bounds.northeast.longitude);
        bbox.minLat = Math.min(bounds.southwest.latitude, bounds.northeast.latitude);
        bbox.maxLng = Math.max(bounds.southwest.longitude, bounds.northeast.longitude);
        bbox.maxLat = Math.max(bounds.southwest.latitude, bounds.northeast.latitude);
        return bbox;
    }

    public LatLngBounds toLatLngBounds() {
        return new LatLngBounds(new LatLng(minLat, minLng), new LatLng(maxLat, maxLng));
    }
}