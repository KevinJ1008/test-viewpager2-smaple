package com.kevinj1008.testviewpager2sample.interfaces

import android.util.SparseIntArray

interface IViewPagerFeature {
    /**
     * Enable auto-scroll mode
     *
     * @param intervalInMillis The interval time to scroll in milliseconds.
     */
    fun setAutoScroll(intervalInMillis: Int)

    /**
     * Enable auto-scroll mode with special interval times
     * @param intervalInMillis The default time to scroll
     * @param intervalArray The special interval to scroll, in responding to each frame
     */
    fun setAutoScroll(intervalInMillis: Int, intervalArray: SparseIntArray?)

    /**
     * Disable auto-scroll mode
     */
    fun disableAutoScroll()

    /**
     * Set an infinite loop
     *
     * @param enable enable or disable
     */
    fun setInfiniteLoop(enable: Boolean)

    /**
     * Set the aspect ratio for UltraViewPager.
     *
     * @param ratio
     */
    fun setRatio(ratio: Float)

    /**
     * Scroll to the last page, and return to the first page when the last page is reached.
     */
    fun scrollLastPage(): Boolean

    /**
     * Scroll to the next page, and return to the first page when the last page is reached.
     */
    fun scrollNextPage(): Boolean

    /**
     * Set multi-screen mode , the aspect ratio of PageViewer should less than or equal to 1.0f
     */
    fun setMultiScreen(ratio: Float)

    /**
     * Adjust the height of the ViewPager to the height of child automatically.
     */
    fun setAutoMeasureHeight(status: Boolean)

    /**
     * Adjust the height of child item view with aspect ratio.
     *
     * @param ratio aspect ratio
     */
    fun setItemRatio(ratio: Double)

    /**
     * Set the gap between two pages in pixel
     *
     * @param pixel
     */
    fun setHGap(pixel: Int)

    /**
     * Set item margin
     *
     * @param left   the left margin in pixels
     * @param top    the top margin in pixels
     * @param right  the right margin in pixels
     * @param bottom the bottom margin in pixels
     */
    fun setItemMargin(left: Int, top: Int, right: Int, bottom: Int)

    /**
     * Set margins for this ViewPager
     *
     * @param left  the left margin in pixels
     * @param right the right margin in pixels
     */
    fun setScrollMargin(left: Int, right: Int)

    /**
     * The items.size() would be scale to item.size()*infiniteRatio in fact
     *
     * @param infiniteRatio
     */
    fun setInfiniteRatio(infiniteRatio: Int)
}