package com.leo.practiceproject.transition;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.transition.Transition;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;

import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class CustomSharedElementCallback extends SharedElementCallback {

    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static class ShapeableViewShapeProvider implements ShapeProvider {
        @Nullable
        @Override
        public ShapeAppearanceModel provideShape(@NonNull View sharedElement) {
            return sharedElement instanceof Shapeable
                    ? ((Shapeable) sharedElement).getShapeAppearanceModel()
                    : null;
        }
    }

    public interface ShapeProvider {
        @Nullable
        ShapeAppearanceModel provideShape(@NonNull View sharedElement);
    }

    private boolean entering = true;

    private boolean sharedElementReenterTransitionEnabled = false;
    private boolean transparentWindowBackgroundEnabled = true;
    @Nullable
    private static WeakReference<View> capturedSharedElement;
    @Nullable
    private ShapeProvider shapeProvider = new ShapeableViewShapeProvider();

    /**
     *最先调用，用于动画开始前替换ShareElements，比如在Activity B翻过若干页大图之后，返回Activity A
     *的时候需要缩小回到对应的小图，就需要在这里进行替换
     */
    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        if (!names.isEmpty() && !sharedElements.isEmpty()) {
            View sharedElement = sharedElements.get(names.get(0));
            if (sharedElement != null) {
                Activity activity = getActivity(sharedElement.getContext());
                if (activity != null) {
                    Window window = activity.getWindow();
                    if (entering) {
                        setUpEnterTransform(window);
                    } else {
                        setUpReturnTransform(activity, window);
                    }
                }
            }
        }
    }

    /**
     *在这里会把ShareElement里值得记录的信息存到为Parcelable格式，以发送到Activity B
     *默认处理规则是ImageView会特殊记录Bitmap、ScaleType、Matrix，其它View只记录大小和位置
     */
    @Override
    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
        capturedSharedElement = new WeakReference<>(sharedElement);
        return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
    }


    /**
     *表示ShareElement已经全部就位，可以开始动画了
     */
    @Override
    public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
        super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
    }

    /**
     *在之前的步骤里(onMapSharedElements)被从ShareElements列表里除掉的View会在此回调，
     *不处理的话默认进行alpha动画消失
     */
    @Override
    public void onRejectSharedElements(List<View> rejectedSharedElements) {
        super.onRejectSharedElements(rejectedSharedElements);
    }


    /**
     *在这里会把Activity A传过来的Parcelable数据，重新生成一个View，这个View的大小和位置会与Activity A里的
     *ShareElement一致，
     */
    @Override
    public View onCreateSnapshotView(Context context, Parcelable snapshot) {
        View snapshotView = super.onCreateSnapshotView(context, snapshot);
        // 这一段是针对他们固定view做的
//        if (snapshotView != null && capturedSharedElement != null && shapeProvider != null) {
//            View sharedElement = capturedSharedElement.get();
//            if (sharedElement != null) {
//                ShapeAppearanceModel shapeAppearanceModel = shapeProvider.provideShape(sharedElement);
//                if (shapeAppearanceModel != null) {
//                    // Set shape appearance as snapshot view tag, which will be used by the transform.
//                    snapshotView.setTag(R.id.mtrl_motion_snapshot_view, shapeAppearanceModel);
//                }
//            }
//        }
        return snapshotView;
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
    }


    private void setUpEnterTransform(final Window window) {
        Transition transition = window.getSharedElementEnterTransition();
        if (transition instanceof CustomTransition) {
            CustomTransition transform = (CustomTransition) transition;
            if (!sharedElementReenterTransitionEnabled) {
                window.setSharedElementReenterTransition(null);
            }
//            if (transparentWindowBackgroundEnabled) {
//                updateBackgroundFadeDuration(window, transform);
//                transform.addListener(
//                        new TransitionListenerAdapter() {
//                            @Override
//                            public void onTransitionStart(Transition transition) {
//                                removeWindowBackground(window);
//                            }
//
//                            @Override
//                            public void onTransitionEnd(Transition transition) {
//                                restoreWindowBackground(window);
//                            }
//                        });
//            }
        }
    }

    private void setUpReturnTransform(final Activity activity, final Window window) {
        Transition transition = window.getSharedElementReturnTransition();
        if (transition instanceof CustomTransition) {
            CustomTransition transform = (CustomTransition) transition;
//            transform.setHoldAtEndEnabled(true);
//            transform.addListener(
//                    new TransitionListenerAdapter() {
//                        @Override
//                        public void onTransitionEnd(Transition transition) {
//                            // Make sure initial shared element view is visible to avoid blinking effect.
//                            if (capturedSharedElement != null) {
//                                View sharedElement = capturedSharedElement.get();
//                                if (sharedElement != null) {
//                                    sharedElement.setAlpha(1);
//                                    capturedSharedElement = null;
//                                }
//                            }
//
//                            // Prevent extra transform from happening after the return transform is finished.
//                            activity.finish();
//                            activity.overridePendingTransition(0, 0);
//                        }
//                    });
//            if (transparentWindowBackgroundEnabled) {
//                updateBackgroundFadeDuration(window, transform);
//                transform.addListener(
//                        new TransitionListenerAdapter() {
//                            @Override
//                            public void onTransitionStart(Transition transition) {
//                                removeWindowBackground(window);
//                            }
//                        });
//            }
        }
    }
}
