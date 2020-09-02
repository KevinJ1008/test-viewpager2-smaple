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

import android.graphics.drawable.Drawable;

/**
 * Created by mikeafc on 15/11/25.
 */
public interface IUltraIndicatorBuilder {

    /**
     * Set focused color for indicator.
     *
     * @param focusColor
     * @return
     */
    IUltraIndicatorBuilder setFocusColor(int focusColor);

    /**
     * Set normal color for indicator.
     *
     * @param normalColor
     * @return
     */
    IUltraIndicatorBuilder setNormalColor(int normalColor);

    IUltraIndicatorBuilder setFocusDrawable(Drawable focusDrawable);

    IUltraIndicatorBuilder setNormalDrawable(Drawable normalDrawable);

    /**
     * Set spacing between indicator item ，the default value is item's height.
     *
     * @param indicatorPadding
     * @return
     */
    IUltraIndicatorBuilder setIndicatorSpacing(int indicatorPadding);

    /**
     * Set the corner radius of the indicator item.
     *
     * @param radius
     * @return
     */
    IUltraIndicatorBuilder setRadius(int radius);

    /**
     * Set offsets for indicator.
     *
     * @param horizontal the horizontal offset to the closest bound of ViewPager, this value will be ignored
     *                   if use a Gravity.CENTER_HORIZONTAL alignment.
     * @param vertical   the vertical offset relative to ViewPager
     * @return
     */
    IUltraIndicatorBuilder setOffset(int horizontal, int vertical);

    IUltraIndicatorBuilder setOutside();

    IUltraIndicatorBuilder setInside();

    IUltraIndicatorBuilder setGravity(int gravity);

    /**
     * Combine all of the options that have been set and return a new IUltraIndicatorBuilder object.
     */
    void build();
}
