package com.leo.practiceproject.touchlearn;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TouchTextView extends androidx.appcompat.widget.AppCompatTextView {
    public TouchTextView(@NonNull Context context) {
        super(context);
    }

    public TouchTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("leo7723", "TouchTextView dispatchTouchEvent " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("leo7723", "TouchTextView onTouchEvent " + event.getAction());
        return super.onTouchEvent(event);
    }

}
