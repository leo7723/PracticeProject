package com.leo.practiceproject.today;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.leo.practiceproject.R;

public class TransformSecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        // Set up shared element transition
        findViewById(android.R.id.content).setTransitionName("shared_element_end_root");
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementEnterTransition(buildContainerTransform(true));
        getWindow().setSharedElementReturnTransition(buildContainerTransform(false));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_load);
        ImageView imageView = findViewById(R.id.top_image);
        imageView.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
        });
        imageView.setPadding(0, getStatusBarHeight(), 0, 0);

    }

    private MaterialContainerTransform buildContainerTransform(boolean entering) {
        MaterialContainerTransform transform = new MaterialContainerTransform(this, entering);
        // Use all 3 container layer colors since this transform can be configured using any fade mode
        // and some of the start views don't have a background and the end view doesn't have a
        // background.
        transform.setAllContainerColors(
                MaterialColors.getColor(findViewById(android.R.id.content), com.google.android.material.R.attr.colorSurface));
        transform.addTarget(android.R.id.content);
        transform.setDuration(600);
        transform.setInterpolator(new FastOutSlowInInterpolator());
//        transform.setFadeMode(getFadeMode());
//        transform.setDrawDebugEnabled(isDrawDebugEnabled());
        return transform;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}