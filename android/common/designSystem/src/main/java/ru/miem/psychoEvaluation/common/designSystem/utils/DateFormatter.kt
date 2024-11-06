package ru.miem.psychoEvaluation.common.designSystem.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
