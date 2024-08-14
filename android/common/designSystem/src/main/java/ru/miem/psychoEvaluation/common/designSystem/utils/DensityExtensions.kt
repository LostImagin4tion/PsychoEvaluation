package ru.miem.psychoEvaluation.common.designSystem.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.Px

private val displayMetrics = Resources.getSystem().displayMetrics

fun Int.pxToDpf(): Float = this / displayMetrics.density

fun Int.pxToDp(): Int = pxToDpf().toInt()

val Int.dpd: Double
    @Px
    get() = this * displayMetrics.density.toDouble()

val Int.dpf: Float
    @Px
    get() = this * displayMetrics.density

val Int.dp: Int
    @Px
    @JvmName("dpToPx")
    get() = dpf.toInt()

val Int.sp: Float
    @Px
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), displayMetrics)

val Float.dpf: Float
    @Px
    get() = this * displayMetrics.density

val Float.dp: Int
    @Px
    get() = dpf.toInt()