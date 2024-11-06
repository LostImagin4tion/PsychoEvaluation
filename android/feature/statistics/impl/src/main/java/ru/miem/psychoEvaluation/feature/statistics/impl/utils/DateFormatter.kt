package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.getParsedDate(
    format: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault()
): String {
    val df = SimpleDateFormat(format, locale)
    return df.format(this)
}

fun parsedDateForApi(notParsedDate: Date): String {
    return notParsedDate.getParsedDate("yyyy.MM.dd")
}
