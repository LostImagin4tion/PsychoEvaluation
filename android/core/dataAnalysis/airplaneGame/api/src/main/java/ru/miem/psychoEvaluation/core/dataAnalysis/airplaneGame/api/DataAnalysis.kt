package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api

import kotlinx.coroutines.flow.Flow

data class Borders(
    val method: String,
    val upperLimit: Double,
    val lowerLimit: Double,
)

interface DataAnalysis {
    fun findPreparationData(dataFlow: Flow<Int>): Flow<Int>
    fun findDataBorders(y: List<Int>): Borders
    fun getNormalizedValue(value: Double, borders: Borders): Double
    fun increaseDifficulty(borders: Borders): Borders
    fun decreaseDifficulty(borders: Borders): Borders
}
