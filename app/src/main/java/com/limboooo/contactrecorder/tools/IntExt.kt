package com.limboooo.contactrecorder.tools

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.pow
import kotlin.math.sqrt

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

infix fun Int.diagonalDistance(b: Int): Float = let {
    return sqrt(this.toDouble().pow(2.0) + b.toDouble().pow(2.0)).toFloat()
}