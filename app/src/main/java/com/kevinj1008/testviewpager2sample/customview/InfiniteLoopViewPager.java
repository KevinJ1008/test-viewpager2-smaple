/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.kevinj1008.testviewpager2sample.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.kevinj1008.testviewpager2sample.R;
import com.kevinj1008.testviewpager2sample.customview.transformer.IBaseTransformer;
import com.kevinj1008.testviewpager2sample.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified from https://github.com/alibaba/UltraViewPager <br />
 * </p>
 * Refactor {@link ViewPager} to {@link ViewPager2} to support RTL
 */
public class InfiniteLoopViewPager extends RelativeLayout implements IInfiniteLoopViewPagerFeature,
        InfiniteLoopViewPagerAdapter.IUltraViewPagerCenterListener {
    private static final String TAG = InfiniteLoopViewPager.class.getSimpleName();

    private static final int DEFAULT_AUTO_SCROLL_DURATION = 800;
    private static final int DEFAULT_AUTO_SCROLL_INTERVAL = 7000;
    private int mIntervalInMillis = 7000;

    private ViewPager2 viewPager;
    private InfiniteLoopViewPagerAdapter viewPagerAdapter;

    private DelegateOnPageChangeListener mDelegateOnPageChangeListener = new DelegateOnPageChangeListener();
    private List<ViewPager2.OnPageChangeCallback> mOnPageChangeListeners = new ArrayList<>(2);

    private TimerHandler timer;
    /**
     * In some case, auto scroll will be trigger off, eg. no data
     */
    private boolean mIsAutoScrollPaused;
    private ValueAnimator mAutoScrollAnimator;
    private TimerHandler.ITimerCallback mITimerCallback = new TimerHandler.ITimerCallback() {
        @Override
        public void callBack() {
            scrollNextPage();
        }
    };
    private AutoScrollAnimatorUpdateListener mAnimatorUpdateListener = new AutoScrollAnimatorUpdateListener();
    private int mAutoScrollAnimatorEndValue = 0;
    private int mAutoScrollDuration = 0;
    private int mAutoScrollInterval = 0;

    private int mLastX;
    private int mLastY;

    /**
     * For view bounds detection;
     */
    private Rect mRect = new Rect();
    private int mScreenWidth;

    private ViewPager2.PageTransformer pageTransformer;
    private boolean enableLoop;
    private int pageMargin;

    public InfiniteLoopViewPager(Context context) {
        super(context);
        initView(context, null);
    }

    public InfiniteLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public InfiniteLoopViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mScreenWidth = ViewUtils.getScreenWidth();
        viewPager = new ViewPager2(getContext());
        addView(viewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        viewPager.unregisterOnPageChangeCallback(mDelegateOnPageChangeListener);
        viewPager.registerOnPageChangeCallback(mDelegateOnPageChangeListener);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InfiniteLoopViewPager);
            setAutoScroll(ta.getInt(R.styleable.InfiniteLoopViewPager_autoScroll, 0));
            setInfiniteLoop(ta.getBoolean(R.styleable.InfiniteLoopViewPager_infiniteLoop, false));
            ta.recycle();

            viewPager.setId(R.id.test_wrap_view_pager2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // update auto scroll start-end value
        int scrollEnd = computeAnimatorEndValue();
        if (scrollEnd != mAutoScrollAnimatorEndValue && mAutoScrollAnimator != null) {
            mAutoScrollAnimatorEndValue = scrollEnd;
            mAutoScrollAnimator.setIntValues(0, mAutoScrollAnimatorEndValue);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    @Override
    protected void onVisibilityChanged(@NotNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        stopTimer();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        startTimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                if (timer != null) {
                    stopTimer();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (timer != null) {
                    startTimer();
                }
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            final float offsetX = getScrollX() - viewPager.getLeft();
            final float offsetY = getScrollY() - viewPager.getTop();
            event.offsetLocation(offsetX, offsetY);
            boolean result = viewPager.dispatchTouchEvent(event);
            event.offsetLocation(-offsetX, -offsetY);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewPager.dispatchTouchEvent(event);
    }

    @Override
    public void setAutoScroll(int intervalInMillis) {
        mIntervalInMillis = intervalInMillis;
        setAutoScroll(intervalInMillis, DEFAULT_AUTO_SCROLL_DURATION);
    }

    @Override
    public void setAutoScroll(int intervalInMillis, int scrollDurationInMillis) {
        if (0 == intervalInMillis || 0 == scrollDurationInMillis) {
            return;
        }

        if (timer != null) {
            disableAutoScroll();
        }
        initAutoScrollAnimator();
        if (mAutoScrollDuration != scrollDurationInMillis) {
            mAutoScrollDuration = scrollDurationInMillis;
            mAutoScrollAnimator.setDuration(mAutoScrollDuration);
        }

        mAutoScrollInterval = intervalInMillis;
        timer = new TimerHandler(mITimerCallback, intervalInMillis);
        startTimer();
    }

    @Override
    public void disableAutoScroll() {
        stopTimer();
        timer = null;
    }

    @Override
    public void resumeAutoScroll() {
        setAutoScroll(mIntervalInMillis);
    }

    @Override
    public void pauseAutoScroll() {
        disableAutoScroll();
    }

    @Override
    public boolean isInfiniteLoop() {
        return viewPagerAdapter != null && viewPagerAdapter.isEnableLoop();
    }

    @Override
    public void setInfiniteLoop(boolean enableLoop) {
        this.enableLoop = enableLoop;
    }

    /**
     * Set viewPager's margin for item
     *
     * @param marginPixels
     */
    @Override
    public void setPageMargin(int marginPixels) {
        this.pageMargin = marginPixels;
        //ScrollEventAdapter will get wrong value due to RTL condition mechanism not working correctly,
        // so just try-catch block to protect
        try {
            viewPager.setPageTransformer(new MarginPageTransformer(marginPixels));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void scrollNextPage() {
        if (viewPager != null && viewPager.getAdapter() != null
                && viewPager.getAdapter().getItemCount() > 0
                && viewPager.getChildCount() > 0) {
            animatePagerTransition();
        }
    }

    @Override
    public void setScrollMargin(int start, int end) {
        viewPager.setPaddingRelative(start, 0, end, 0);
    }

    @Override
    public void center() {
        setCurrentItem(0);
    }

    @Override
    public void resetPosition() {
        setCurrentItem(getCurrentItem());
    }

    @Override
    public int getItemPositionInLoop(int realPosition) {
        if (viewPagerAdapter != null && viewPagerAdapter.getItemCount() != 0 && viewPagerAdapter.isEnableLoop()) {
            realPosition = viewPagerAdapter.getItemCount() / 2 + realPosition % viewPagerAdapter.getRealCount();
        }
        return realPosition;
    }

    @Override
    public int getCurrentItem() {
        if (viewPagerAdapter != null && viewPagerAdapter.getItemCount() != 0) {
            int position = viewPager.getCurrentItem();
            return position % viewPagerAdapter.getRealCount();
        }
        return viewPager.getCurrentItem();
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (viewPager.isFakeDragging()) {
            viewPager.endFakeDrag();
        }
        viewPager.setCurrentItem(getItemPositionInLoop(item), smoothScroll);
    }

    @Override
    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
        viewPager.setClipChildren(clipChildren);
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
        super.setClipToPadding(clipToPadding);
        viewPager.setClipToPadding(clipToPadding);
    }

    @Override
    public void setPageTransformer(IBaseTransformer transformer) {
        this.pageTransformer = transformer;
        //ScrollEventAdapter will get wrong value due to RTL condition mechanism not working correctly,
        // so just try-catch block to protect
//        try {
            viewPager.setPageTransformer(transformer);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void setInfiniteRatio(int infiniteRatio) {
        if (viewPager.getAdapter() != null
                && viewPager.getAdapter() instanceof InfiniteLoopViewPagerAdapter) {
            ((InfiniteLoopViewPagerAdapter) viewPager.getAdapter()).setInfiniteRatio(infiniteRatio);
        }
    }

    @Override
    public void updateTransforming() {
        if (pageTransformer != null) {
            final int scrollX = getScrollX();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final float transformPos = (float) (child.getLeft() - scrollX)
                        / (viewPager.getMeasuredWidth()
                        - viewPager.getPaddingStart()
                        - viewPager.getPaddingEnd());
                pageTransformer.transformPage(child, transformPos);
            }
        }
    }


    @Override
    public RecyclerView.Adapter getInternalAdapter() {
        return viewPager.getAdapter();
    }

    public void stopCurrentScrollAnimation() {
        if (mAutoScrollAnimator != null) {
            mAutoScrollAnimator.cancel();
        }
    }

    public boolean isAutoScrollEnabled() {
        return timer != null;
    }

    /**
     * recommend {@link #addOnPageChangeListener(ViewPager2.OnPageChangeCallback)}
     *
     * @see #addOnPageChangeListener(ViewPager2.OnPageChangeCallback)
     * @see #removeOnPageChangeListener(ViewPager2.OnPageChangeCallback)
     * @see #clearOnPageChangeListeners()
     */
    public void setOnPageChangeListener(ViewPager2.OnPageChangeCallback listener) {
        clearOnPageChangeListeners();
        addOnPageChangeListener(listener);
    }

    public void addOnPageChangeListener(ViewPager2.OnPageChangeCallback listener) {
        if (listener != null) {
            mOnPageChangeListeners.remove(listener);
            mOnPageChangeListeners.add(listener);
        }
    }

    public void removeOnPageChangeListener(ViewPager2.OnPageChangeCallback listener) {
        if (listener != null) {
            mOnPageChangeListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListeners() {
        mOnPageChangeListeners.clear();
    }

    public void setOffscreenPageLimit(int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }

    public int getOffscreenPageLimit() {
        return viewPager.getOffscreenPageLimit();
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        return viewPager.getAdapter() == null ? null : ((InfiniteLoopViewPagerAdapter) viewPager.getAdapter()).getAdapter();
    }

    /**
     * delegate viewpager
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        // Stop running animator
        stopCurrentScrollAnimation();

        if (adapter == null || adapter.getItemCount() <= 0) {
            // no data, disable auto scroll
            disableAutoScroll();
            mIsAutoScrollPaused = true;
        }

        setInfiniteLoopViewPagerAdapter(adapter);
        if (adapter != null) {
            viewPagerAdapter = (InfiniteLoopViewPagerAdapter) viewPager.getAdapter();

            //bind ViewPager and Adapter，update to disable animate
            if (viewPagerAdapter != null) {
                viewPagerAdapter.setViewPager(this);
            }
            if (mIsAutoScrollPaused && adapter.getItemCount() > 0) {
                setAutoScroll(mAutoScrollInterval, mAutoScrollDuration);
                mIsAutoScrollPaused = false;
            }
        } else {
            viewPagerAdapter = null;
        }
    }

    public ViewPager2 getViewPager() {
        return viewPager;
    }

    public void notifyDataSetChanged() {
        if (viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    public void animatePagerTransition() {
        if (mAutoScrollAnimator != null && !mAutoScrollAnimator.isRunning()) {
            if (viewPager.beginFakeDrag()) {
                mAutoScrollAnimator.start();
            }
        }
    }

    public ViewPager2.PageTransformer getTransformer() {
        return pageTransformer;
    }

    private void setInfiniteLoopViewPagerAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            viewPager.setAdapter(null);
            return;
        }

        InfiniteLoopViewPagerAdapter recyclerViewAdapter;
        if (adapter instanceof InfiniteLoopViewPagerAdapter) {
            recyclerViewAdapter = (InfiniteLoopViewPagerAdapter) adapter;
        } else {
            recyclerViewAdapter = new InfiniteLoopViewPagerAdapter(adapter);
        }

        recyclerViewAdapter.setCenterListener(this);
        recyclerViewAdapter.setEnableLoop(enableLoop);
        viewPager.setAdapter(recyclerViewAdapter);
    }

    private void startTimer() {
        if (timer != null) {
            timer.setListener(mITimerCallback);
            timer.trigger();
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.setListener(null);
            timer.stop();
        }
    }

    private void onAnimatorEnd() {
        if (viewPagerAdapter != null && viewPagerAdapter.getRealCount() > 0) {
            if (viewPager.isFakeDragging()) {
                viewPager.endFakeDrag();
            }
        }
        mAnimatorUpdateListener.reset();
    }

    private void initAutoScrollAnimator() {
        if (mAutoScrollAnimator == null) {
            if (mAutoScrollAnimatorEndValue == 0) {
                mAutoScrollAnimatorEndValue = computeAnimatorEndValue();
            }
            mAutoScrollAnimator = ValueAnimator.ofInt(0, mAutoScrollAnimatorEndValue);
            mAutoScrollAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    // Unnecessary
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onAnimatorEnd();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    onAnimatorEnd();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // Unnecessary
                }
            });

            mAutoScrollAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAutoScrollAnimator.addUpdateListener(mAnimatorUpdateListener);
            mAutoScrollAnimator.setDuration(mAutoScrollDuration);
        }
    }

    private int computeAnimatorEndValue() {
        return viewPager.getMeasuredWidth() - viewPager.getPaddingStart() + pageMargin;
    }

    private class AutoScrollAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private int oldDragPosition = 0;

        void reset() {
            oldDragPosition = 0;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int dragPosition = (Integer) animation.getAnimatedValue();
            int dragOffset = dragPosition - oldDragPosition;
            oldDragPosition = dragPosition;
            if (viewPager.getChildCount() > 0) {
                viewPager.fakeDragBy(-dragOffset);
            }
        }
    }

    /**
     * A delegate <code>OnPageChangeListener</code> to make sure all positions are converted properly
     */
    private class DelegateOnPageChangeListener extends ViewPager2.OnPageChangeCallback {
        private boolean check() {
            return viewPagerAdapter != null &&
                    (!mOnPageChangeListeners.isEmpty());
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (check()) {
                position = viewPagerAdapter.getRealPosition(position);
                int size = mOnPageChangeListeners == null ? 0 : mOnPageChangeListeners.size();
                for (int i = 0; i < size; i++) {
                    ViewPager2.OnPageChangeCallback onPageChangeListener = mOnPageChangeListeners.get(i);
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (check()) {
                position = viewPagerAdapter.getRealPosition(position);
                for (ViewPager2.OnPageChangeCallback l : mOnPageChangeListeners) {
                    l.onPageSelected(position);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (check()) {
                for (ViewPager2.OnPageChangeCallback l : mOnPageChangeListeners) {
                    l.onPageScrollStateChanged(state);
                }
            }
        }
    }
}
