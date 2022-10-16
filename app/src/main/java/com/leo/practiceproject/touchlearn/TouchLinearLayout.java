package com.leo.practiceproject.touchlearn;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class TouchLinearLayout extends LinearLayout {
    public TouchLinearLayout(Context context) {
        super(context);
    }

    public TouchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("leo7723", "TouchLinearLayout dispatchTouchEvent " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    //    /**
//     * Constant for {@link #getActionMasked}: A pressed gesture has started, the
//     * motion contains the initial starting location.
//     * <p>
//     * This is also a good time to check the button state to distinguish
//     * secondary and tertiary button clicks and handle them appropriately.
//     * Use {@link #getButtonState} to retrieve the button state.
//     * </p>
//     */
//    public static final int ACTION_DOWN             = 0;
//
//    /**
//     * Constant for {@link #getActionMasked}: A pressed gesture has finished, the
//     * motion contains the final release location as well as any intermediate
//     * points since the last down or move event.
//     */
//    public static final int ACTION_UP               = 1;
//
//    /**
//     * Constant for {@link #getActionMasked}: A change has happened during a
//     * press gesture (between {@link #ACTION_DOWN} and {@link #ACTION_UP}).
//     * The motion contains the most recent point, as well as any intermediate
//     * points since the last down or move event.
//     */
//    public static final int ACTION_MOVE             = 2;
//
//    /**
//     * Constant for {@link #getActionMasked}: The current gesture has been aborted.
//     * You will not receive any more points in it.  You should treat this as
//     * an up event, but not perform any action that you normally would.
//     */
//    public static final int ACTION_CANCEL           = 3;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("leo7723", "TouchLinearLayout onTouchEvent " + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("leo7723", "TouchLinearLayout onInterceptTouchEvent " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }
}
