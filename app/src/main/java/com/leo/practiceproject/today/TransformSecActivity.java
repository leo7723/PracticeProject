package com.leo.practiceproject.today;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.app.Instrumentation;
import android.os.Build;
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
import com.leo.practiceproject.transition.CustomSharedElementCallback;
import com.leo.practiceproject.transition.CustomTransition;

import java.util.ArrayList;
import java.util.List;

public class TransformSecActivity extends AppCompatActivity {

    ArrayList<String> sharedElementNames;

    String PENDING_EXIT_SHARED_ELEMENTS = "android:pendingExitSharedElements";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        // Set up shared element transition
//        findViewById(android.R.id.content).setTransitionName("shared_element_end_root");
//        if (savedInstanceState!=null) {
////            savedInstanceState.getStringArrayList(PENDING_EXIT_SHARED_ELEMENTS);
//            savedInstanceState.putStringArrayList(PENDING_EXIT_SHARED_ELEMENTS, new ArrayList<>());
//        }
//        MyCallBack callBack = new MyCallBack();
        SharedElementCallback callBack = new CustomSharedElementCallback();
        setEnterSharedElementCallback(callBack);
//        getWindow().setSharedElementEnterTransition(buildContainerTransform(true));
//        getWindow().setSharedElementReturnTransition(buildContainerTransform(false));
        getWindow().setSharedElementEnterTransition(new CustomTransition());
        getWindow().setSharedElementReturnTransition(new CustomTransition());
//        getWindow().setSharedElementsUseOverlay(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_load);
        findViewById(R.id.card).setTransitionName("shared_element_end_root");
//        ImageView imageView = findViewById(R.id.top_image);
//        imageView.setAccessibilityDelegate(new View.AccessibilityDelegate(){
//            @Override
//            public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
//                return super.onRequestSendAccessibilityEvent(host, child, event);
//            }
//        });
//        imageView.setPadding(0, getStatusBarHeight(), 0, 0);

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

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (outState.containsKey("InstrumentationFixBug") && outState.getBoolean("InstrumentationFixBug")) {
//            return;
//        }
//    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && sharedElementNames != null) {
//                outState.putStringArrayList(PENDING_EXIT_SHARED_ELEMENTS, sharedElementNames);
//        }
//    }
//
//    @Override
//    public void finishAfterTransition() {
//        super.finishAfterTransition();
////        finish();
//    }
//
//    @Override
//    protected void onStop() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isFinishing()) {
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("InstrumentationFixBug", true);
//            new Instrumentation().callActivityOnSaveInstanceState(this, bundle);
//        }
//        super.onStop();
//    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }

    class MyCallBack extends MaterialContainerTransformSharedElementCallback {

        @Override
        public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
            super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
            TransformSecActivity.this.sharedElementNames = new ArrayList<>(sharedElementNames);
        }
    }
}