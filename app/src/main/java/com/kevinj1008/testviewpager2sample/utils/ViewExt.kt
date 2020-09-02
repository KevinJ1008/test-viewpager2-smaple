@file:JvmName("ViewUtils")
package com.kevinj1008.testviewpager2sample.utils

import android.content.res.Resources

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}