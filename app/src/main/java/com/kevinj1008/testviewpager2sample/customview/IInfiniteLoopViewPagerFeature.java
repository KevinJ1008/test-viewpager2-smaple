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

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.kevinj1008.testviewpager2sample.customview.transformer.IBaseTransformer;

interface IInfiniteLoopViewPagerFeature {

    /**
     * Enable auto-scroll mode
     *
     * @param intervalInMillis The interval time to scroll in milliseconds.
     */
    void setAutoScroll(int intervalInMillis);

    /**
     * Enable auto-scroll mode with duration
     */
    void setAutoScroll(int intervalInMillis, int scrollDurationInMillis);

    /**
     * Disable auto-scroll mode
     */
    void disableAutoScroll();

    /**
     * Resume auto-scroll with previous set interval
     */
    void resumeAutoScroll();

    /**
     * Same as {@link #disableAutoScroll()}
     */
    void pauseAutoScroll();

    boolean isInfiniteLoop();

    /**
     * Set an infinite loop
     *
     * @param enable enable or disable
     */
    void setInfiniteLoop(boolean enable);

    /**
     * Scroll to the next page, and return to the first page when the last page is reached.
     */
    void scrollNextPage();

    /**
     * Set {@link MarginPageTransformer} for viewPager's margin
     *
     * @param marginPixels
     */
    void setPageMargin(int marginPixels);

    /**
     * Set margins for this ViewPager
     *
     * @param start     the start margin in pixels
     * @param end       the end margin in pixels
     */
    void setScrollMargin(int start, int end);

    /**
     * The items.size() would be scale to item.size()*infiniteRatio in fact
     */
    void setInfiniteRatio(int infiniteRatio);

    /**
     * Get not generated viewPager's adapter
     *
     * @return RecyclerView.Adapter
     */
    RecyclerView.Adapter getInternalAdapter();

    void updateTransforming();

    /**
     * Set item to specific position in loop list
     * {@link #getItemPositionInLoop(int)}
     *
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * Set item to specific position in loop list, and determine is smooth scroll or not
     * {@link #getItemPositionInLoop(int)}
     *
     * @param item
     */
    void setCurrentItem(int item, boolean smoothScroll);

    /**
     * Get current item in list but not loop list,
     * if want to get position in loop list check {@link #getItemPositionInLoop(int)}
     *
     * @return current item
     */
    int getCurrentItem();

    /**
     * Get position in loop list
     *
     * @param realPosition
     * @return realPosition
     */
    int getItemPositionInLoop(int realPosition);

    /**
     * Set a {@link ViewPager2.PageTransformer} for viewPager2
     *
     * @see MarginPageTransformer
     * @see CompositePageTransformer
     *
     * @param transformer
     */
    void setPageTransformer(IBaseTransformer transformer);
}
