package ru.miem.psychoEvaluation.feature.statistics.impl.utils

import androidx.compose.runtime.MutableState
import ru.miem.psychoEvaluation.feature.statistics.impl.ui.StatisticsCardData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class CardUpdate {

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    fun onUpdateCards(data1: MutableState<Date?>, data2: MutableState<Date?>): MutableList<StatisticsCardData?> {
        val cardsList: MutableList<StatisticsCardData?> = mutableListOf(null)
        if (data2.value != null && data1.value != null) {
            val date1 = data1.value?.getParsedDate("yyyy-MM-dd")
            val date2 = data2.value?.getParsedDate("yyyy-MM-dd")
            for (i in LocalDate.parse(date1)..LocalDate.parse(date2) step 1) {
                cardsList.add(
                    StatisticsCardData(
                        i.format(DateTimeFormatter.ofPattern("d MMMM")),
                        (RANDOM_VAL_1..RANDOM_VAL_2).random(), "6:05", "4:55"
                    )
                )
            }
        } else if (data1.value == null) {
            cardsList.add(
                StatisticsCardData(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM")),
                    (RANDOM_VAL_1..RANDOM_VAL_2).random(), "6:05", "4:55"
                )
            )
        } else {
            val date1 = data1.value!!.getParsedDate("d MMMM")
            cardsList.add(StatisticsCardData(date1, (RANDOM_VAL_1..RANDOM_VAL_2).random(), "6:05", "4:55"))
        }
        return cardsList
    }
    companion object {
        private const val RANDOM_VAL_1 = 1
        private const val RANDOM_VAL_2 = 12
    }
}
