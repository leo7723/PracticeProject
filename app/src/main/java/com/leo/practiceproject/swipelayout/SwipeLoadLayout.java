package com.leo.practiceproject.swipelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.practiceproject.R;

public class SwipeLoadLayout extends FrameLayout implements NestedScrollingParent, NestedScrollingChild {

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private NestedScrollingChildHelper nestedScrollingChildHelper;

    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    private FrameLayout mHeaderView;
    private FrameLayout mFooterView;
    private RecyclerView mRecyclerView;

    // RefreshView Height
    private volatile float refreshViewHeight = 0;
    private volatile float loadingViewHeight = 0;

    // Is Refreshing 刷新中快速分发事件给下层
    volatile private boolean mRefreshing = false;

    // 滑动中快速分发事件给下层
    private boolean mNestedScrollInProgress;

    private ViewParent mNestedScrollAcceptedParent;


    public SwipeLoadLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipeLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.refresh_view);
        mFooterView = findViewById(R.id.load_view);
        mRecyclerView = findViewById(R.id.recycler_view);

        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        nestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(false);

        refreshViewHeight = 500;
        loadingViewHeight = 500;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || canChildScrollUp()
                || mRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        nestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return nestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    /**
     * 开始的时候遍历 结束时清空
     * @param axes
     * @return
     */
    @Override
    public boolean startNestedScroll(int axes) {
        boolean result = nestedScrollingChildHelper.startNestedScroll(axes);
        if (result) {
            if (mNestedScrollAcceptedParent == null) {
                ViewParent parent = this.getParent();
                View child = this;
                while (parent != null) {
                    if (ViewParentCompat.onStartNestedScroll(parent, child, this, axes)) {
                        mNestedScrollAcceptedParent = parent;
                        break;
                    }
                    if (parent instanceof View) {
                        child = (View) parent;
                    }
                    parent = parent.getParent();
                }
            }
        }
        return result;
    }

    @Override
    public void stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll();
        if (mNestedScrollAcceptedParent != null) {
            mNestedScrollAcceptedParent = null;
        }
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return nestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    /*********************** parent **********************/

    /**
     * 子view在嵌套滑动之前告诉父布局我要滑动了
     * 父布局返回是否消耗了fling
     * 这里demo不处理fling所以作为子布局继续检查嵌套
     *
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (isNestedScrollingEnabled()) {
            return dispatchNestedPreFling(velocityX, velocityY);
        }
        return false;
    }

    /**
     * 返回是否需要处理Fling
     * 这里是不需要直接交给上层
     *
     * @param target
     * @param velocityX
     * @param velocityY
     * @param consumed
     * @return
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        if(isNestedScrollingEnabled()) {
            return dispatchNestedFling(velocityX, velocityY, consumed);
        }
        return  false;
    }

    // 返回后续是否需要处理滑动
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && !mRefreshing && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // 通过helper处理操作
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // 执行child的适配工作通知上层
        if (isNestedScrollingEnabled()) {
            startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
            mNestedScrollInProgress = true;
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        final int[] parentConsumed = mParentScrollConsumed;
        if (isNestedScrollingEnabled()) {

        }
    }

    /**
     * Whether child view can scroll up
     * @return
     */
    public boolean canChildScrollUp() {
        if (mRecyclerView == null) {
            return false;
        }
        return mRecyclerView.canScrollVertically(-1);
    }
}
