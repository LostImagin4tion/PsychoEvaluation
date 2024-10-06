package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import androidx.compose.runtime.MutableState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class DateIterator(
    startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val stepDays: Long
) : Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive
    override fun next(): LocalDate {
        val next = currentDate

        currentDate = currentDate.plusDays(stepDays)

        return next
    }
}

class DateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1
) :
    Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> =
        DateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)
}

fun Date.getParsedDate(
    format: String = "yyyy-MM-dd HH:mm:ss",
    locale: Locale = Locale.getDefault()
): String {
    val df = SimpleDateFormat(format, locale)
    return df.format(this)
}

fun parsedApiDate(notParsedDate: MutableState<Date?>): String? {
    return notParsedDate.value?.getParsedDate("yyyy.MM.dd")
}
