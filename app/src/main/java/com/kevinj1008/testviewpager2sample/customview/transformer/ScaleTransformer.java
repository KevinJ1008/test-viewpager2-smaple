package com.kevinj1008.testviewpager2sample.customview.transformer;

import android.view.View;

public class ScaleTransformer implements IBaseTransformer {
    private static final float MIN_SCALE = 0.942f;

    private float minScale = MIN_SCALE;

    public ScaleTransformer() {
    }

    public ScaleTransformer(float minScale) {
        this.minScale = minScale;
    }

    @Override
    public void transformPage(View page, float position) {
        float factor;
        if (Float.compare(position, -1F) <= 0 || Float.compare(position, 1F) >= 0) {
            // (-Infinity, -1] [1, +Infinity) 取最小Scale值
            factor = minScale;
        } else if (Float.compare(position, 0.0F) == 0) {
            // position == 0 还原scale
            factor = 1;
        } else {
            // (-1, 0), (0, 1)
            factor = minScale + (1 - Math.abs(position)) * (1 - minScale);
        }

        page.setScaleX(factor);
        page.setScaleY(factor);

        // 调整Translate，确保pageMargin
        if (Float.compare(factor, 1) != 0) {
            int originalWidth = page.getWidth();
            int deltaLeft = (int) ((originalWidth - originalWidth * factor) / 2);
            page.setTranslationX(position > 0 ? -deltaLeft : +deltaLeft);
        } else {
            page.setTranslationX(0);
        }
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
    }
}
