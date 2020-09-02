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

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class InfiniteLoopViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int INFINITE_RATIO = 400;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private boolean enableLoop;
    /**
     * Ensure that the first item is in the middle when enabling loop-mode
     */
    private boolean hasCentered;
    private int infiniteRatio;
    private IUltraViewPagerCenterListener centerListener;
    private InfiniteLoopViewPager mViewPager;
    private Runnable applyTransformerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager != null) {
                mViewPager.updateTransforming();
            }
        }
    };

    public InfiniteLoopViewPagerAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.adapter = adapter;
        infiniteRatio = INFINITE_RATIO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int realPosition = getRealPosition(position);
        // only need to set the center position  when the loop is enabled
        if (!hasCentered) {
            if (adapter.getItemCount() > 0 && getItemCount() > adapter.getItemCount()) {
                centerListener.center();
            }
        }
        hasCentered = true;

        if (mViewPager != null) {
            mViewPager.post(applyTransformerRunnable);
        }
        adapter.onBindViewHolder(holder, realPosition);
    }

    @Override
    public int getItemCount() {
        int count;
        if (enableLoop) {
            if (adapter.getItemCount() == 0) {
                count = 0;
            } else {
                count = adapter.getItemCount() * infiniteRatio;
            }
        } else {
            count = adapter.getItemCount();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return adapter.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        adapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        adapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return adapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        adapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        adapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        adapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter.onDetachedFromRecyclerView(recyclerView);
    }

    public void setViewPager(InfiniteLoopViewPager viewPager) {
        mViewPager = viewPager;
    }

    int getRealPosition(int position) {
        int realPosition = position;
        if (enableLoop && adapter.getItemCount() != 0) {
            realPosition = position % adapter.getItemCount();
        }
        return realPosition;
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    public int getRealCount() {
        return adapter.getItemCount();
    }

    boolean isEnableLoop() {
        return enableLoop;
    }

    void setEnableLoop(boolean status) {
        if (enableLoop == status) {
            return;
        }

        this.enableLoop = status;
        notifyDataSetChanged();
        if (!status) {
            centerListener.resetPosition();
        }
    }

    void setCenterListener(IUltraViewPagerCenterListener listener) {
        centerListener = listener;
    }

    void setInfiniteRatio(int infiniteRatio) {
        this.infiniteRatio = infiniteRatio;
    }

    interface IUltraViewPagerCenterListener {
        void center();

        void resetPosition();
    }
}
