package com.matthewlogan.selectablemapfragment.library;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by matthewlogan on 5/20/14.
 */

public class TouchableMapWrapper extends FrameLayout {

    private OnMapTouchListener mListener;

    public TouchableMapWrapper(Context context) {
        super(context);
    }

    public interface OnMapTouchListener {
        public void onMapTouch(MotionEvent event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mListener.onMapTouch(event);
        return super.dispatchTouchEvent(event);
    }

    public void setOnMapTouchListener(OnMapTouchListener listener) {
        mListener = listener;
    }
}
