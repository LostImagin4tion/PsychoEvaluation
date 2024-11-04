package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import java.time.LocalDate

operator fun LocalDate.rangeTo(other: LocalDate) = DateRange(this, other)

class DateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1
) :
    Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> = DateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = DateRange(start, endInclusive, days)
}

private class DateIterator(
    startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val stepDays: Long
) : Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive

    override fun next(): LocalDate {
        if (!hasNext()) {
            throw NoSuchElementException("Iterator has reached the end")
        }

        val next = currentDate
        currentDate = currentDate.plusDays(stepDays)

        return next
    }
}
