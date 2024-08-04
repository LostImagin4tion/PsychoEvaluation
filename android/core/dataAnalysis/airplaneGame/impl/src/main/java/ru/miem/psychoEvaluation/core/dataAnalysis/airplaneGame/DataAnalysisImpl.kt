package ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.Borders
import ru.miem.psychoEvaluation.core.dataAnalysis.airplaneGame.api.DataAnalysis
import timber.log.Timber
import javax.inject.Inject

@Suppress("MagicNumber")
class DataAnalysisImpl @Inject constructor() : DataAnalysis {

    override fun findPreparationData(dataFlow: Flow<Int>): Flow<Int> {
        return dataFlow.take(PREPARATION_DATA_COUNT)
    }

    override fun findDataBorders(y: List<Int>): Borders {
        require(y.size == PREPARATION_DATA_COUNT) {
            "Size of passed sensor data list must be $PREPARATION_DATA_COUNT"
        }

        val slope = calculateSlope(y)
        val mean = y.sum().toDouble() / y.size

        Timber.tag(TAG).d("HELLO ys $y slope $slope mean $mean")
        val borders = findBorders(slope, mean)
        Timber.tag(TAG).d("HELLO BORDERS $borders")
        return borders
    }

    override fun getNormalizedValue(value: Double, borders: Borders): Double {
        return (value - borders.lowerLimit) / (borders.upperLimit - borders.lowerLimit) * 100
    }

    private fun calculateSlope(y: List<Int>): Double {
        val indices = List(y.size) { index -> index + 1 }

        val sumX = indices.sum()
        val sumY = y.sum()

        val sumXY = indices.zip(y) { x, value -> x * value }.sum()
        val sumXSquared = indices.sumOf { it * it }

        return (indices.size * sumXY - sumX * sumY).toDouble() /
            (indices.size * sumXSquared - sumX * sumX).toDouble()
    }

    private fun findBorders(slope: Double, meanValue: Double): Borders {
        return when {
            slope > -1 && slope < 1 -> Borders(
                method = "Границы в рамках +-10%, прямой тренд",
                upperLimit = meanValue * 1.1,
                lowerLimit = meanValue * 0.9,
            )
            slope < 0 -> Borders(
                method = "Границы в рамках +13% и -7%, убывающий тренд",
                upperLimit = meanValue * 1.13,
                lowerLimit = meanValue * 0.93
            )
            else -> Borders(
                method = "Границы в рамках +7% и -13%, возрастающий тренд",
                upperLimit = meanValue * 1.07,
                lowerLimit = meanValue * 0.87,
            )
        }
    }

    private companion object {
        val TAG: String = DataAnalysisImpl::class.java.simpleName

        const val PREPARATION_DATA_COUNT = 30
    }
}
