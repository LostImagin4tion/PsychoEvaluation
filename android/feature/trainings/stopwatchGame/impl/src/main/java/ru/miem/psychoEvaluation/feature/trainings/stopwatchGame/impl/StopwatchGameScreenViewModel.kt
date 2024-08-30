package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.ActionButtonClickFailed
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.ActionButtonClickSuccessful
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.ArrowJumped
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.HideIndicatorAndBrokenHeart
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameEnded
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameInProgress
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameLoading
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.UiAction
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@Suppress("MagicNumber")
class StopwatchGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _stopwatchGameState = MutableStateFlow<StopwatchGameState>(defaultLoadingState)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    private var isActionButtonClicked = false

    val stopwatchGameState: StateFlow<StopwatchGameState> = _stopwatchGameState
    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
            .launchIn(viewModelScope)
    }

    fun startTimerBeforeStart() {
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultLoadingPeriod)
                    .onEach {  delta ->
                        val state = _stopwatchGameState.value as? StopwatchGameLoading

                        state?.let {
                            val newTime = it.timeBeforeStart - delta

                            check(newTime.inWholeSeconds >= 0)

                            val newState = it.copy(
                                timeBeforeStart = newTime,
                                progress = 1f - newTime / defaultLoadingTimer
                            )
                            _stopwatchGameState.emit(newState)
                        }
                    }
                    .catch {
                        _stopwatchGameState.emit(defaultInProgressState)
                        startGame()
                        this@coroutineScope.cancel()
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private fun startGame() {
        var timeForArrowJump = Random.Default
            .nextInt(8..16)
            .seconds

        tickerFlow(defaultPeriod)
            .runningReduce { accumulator, value ->
                accumulator + value
            }
            .onEach { gameTime ->
                _stopwatchGameState.run {
                    val stopWatchTimeDelta = if (gameTime >= timeForArrowJump) {
                        dispatchAction(ArrowJumped)

                        timeForArrowJump += Random.Default
                            .nextInt(8..16)
                            .seconds

                        arrowJumpDelta
                    } else {
                        defaultPeriod
                    }

                    val state = _stopwatchGameState.value as? StopwatchGameInProgress
                    state?.let {
                        val newState = it.copy(
                            stopwatchTime = it.stopwatchTime + stopWatchTimeDelta,
                            gameTime = createTimeString(gameTime),
                        )

                        emit(newState)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun clickActionButton() {
        isActionButtonClicked = true
    }

    fun restartGame() {
        viewModelScope.launch {
            _stopwatchGameState.emit(defaultInProgressState)
        }
    }

    private fun dispatchAction(action: UiAction) {
        viewModelScope.launch {
            when (action) {
                ArrowJumped -> {
                    _stopwatchGameState.run {
                        val state = value as? StopwatchGameInProgress

                        state?.let {
                            val newState = it.copy(
                                jumpCount = it.jumpCount + 1,
                            )
                            emit(newState)
                        }
                    }
                    startActionButtonTimer()
                }

                is ActionButtonClickSuccessful -> {
                    isActionButtonClicked = false
                    _stopwatchGameState.run {
                        val state = value as? StopwatchGameInProgress

                        state?.let {
                            val newState = it.copy(
                                successfulReactionCount = it.successfulReactionCount + 1,
                                reactionTimings = it.reactionTimings + action.reactionTiming,
                                currentIndicatorType = StopwatchGameInProgress.IndicatorType.Success,
                            )
                            emit(newState)
                        }
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }

                is ActionButtonClickFailed -> {
                    isActionButtonClicked = false
                    _stopwatchGameState.run {
                        val state = value as? StopwatchGameInProgress

                        state?.let {
                            val newHeartsNumber = it.heartsNumber - 1
                            val newState = if (newHeartsNumber == 0) {
                                it.toGameEndedState()
                            } else {
                                it.copy(
                                    heartsNumber = newHeartsNumber,
                                    reactionTimings = it.reactionTimings + action.reactionTiming,
                                    currentIndicatorType = StopwatchGameInProgress.IndicatorType.Failure,
                                )
                            }
                            emit(newState)
                        }
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }

                HideIndicatorAndBrokenHeart -> changeIndicatorType(StopwatchGameInProgress.IndicatorType.Undefined)
            }
        }
    }

    private fun startActionButtonTimer() {
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultPeriod)
                    .scan(0.seconds) { time, value ->
                        time + value
                    }
                    .onEach { time ->
                        if (time < defaultTimeToClickActionButton && isActionButtonClicked) {
                            val action = ActionButtonClickSuccessful(time.inWholeMilliseconds)
                            dispatchAction(action)
                            this@coroutineScope.cancel()
                        }
                        else if (time >= defaultTimeToClickActionButton) {
                            val action = ActionButtonClickFailed(time.inWholeMilliseconds)
                            dispatchAction(action)
                            this@coroutineScope.cancel()
                        }
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private fun startTimerForShowingBrokenHeartAndIndicatorButton() {
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultPeriod)
                    .scan(0.seconds) { time, _ ->
                        time + defaultPeriod
                    }
                    .onEach { timer ->
                        if (timer >= defaultPeriodForHidingIndicatorAndBrokenHeart) {
                            dispatchAction(HideIndicatorAndBrokenHeart)
                            this@coroutineScope.cancel()
                        }
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private suspend fun changeIndicatorType(type: StopwatchGameInProgress.IndicatorType) {
        _stopwatchGameState.run {
            val state = value as? StopwatchGameInProgress
            state?.let {
                emit(it.copy(currentIndicatorType = type))
            }
        }
    }

    private fun tickerFlow(period: Duration) = flow {
        while (true) {
            emit(period)
            delay(period)
        }
    }.cancellable()

    private fun StopwatchGameInProgress.toGameEndedState(): StopwatchGameEnded {
        return StopwatchGameEnded(
            gameTime = gameTime,
            successPercent = successfulReactionCount.toFloat() / jumpCount,
            averageReactionTimeString = createTimeString(reactionTimings.average().milliseconds),
            vigilanceDelta = reactionTimings
                .takeIf { it.isNotEmpty() }
                ?.reduce { accumulated, value -> accumulated - value }
                ?: 0L,
            concentrationDelta = stressData
                .takeIf { it.isNotEmpty() }
                ?.reduce { accumulated, value -> accumulated - value }
                ?: 0,
        )
    }

    private fun createTimeString(duration: Duration): String {
        val minutes = duration.inWholeMinutes.toInt()
        val seconds = duration.inWholeSeconds.toInt() - minutes * 60

        return when {
            minutes >= 10 && seconds >= 10 -> "$minutes:$seconds"
            minutes < 10 && seconds >= 10 -> "0$minutes:$seconds"
            minutes >= 10 && seconds < 10 -> "$minutes:0$seconds"
            else -> "0$minutes:0$seconds"
        }
    }

    private companion object {
        val TAG: String = StopwatchGameScreenViewModel::class.java.simpleName

        val defaultLoadingTimer = 3.seconds
        val defaultLoadingPeriod = 10.milliseconds
        val defaultPeriod = 100.milliseconds
        val arrowJumpDelta = 3.seconds
        val defaultTimeToClickActionButton = 3.seconds
        val defaultPeriodForHidingIndicatorAndBrokenHeart = 2.seconds

        val defaultLoadingState = StopwatchGameLoading(
            timeBeforeStart = defaultLoadingTimer,
            progress = 1.0
        )
        val defaultInProgressState = StopwatchGameInProgress(
            stopwatchTime = 0.seconds,
            gameTime = "00:00",
            heartsNumber = 3,
            jumpCount = 0,
            successfulReactionCount = 0,
            reactionTimings = emptyList(),
            stressData = emptyList(),
            currentIndicatorType = StopwatchGameInProgress.IndicatorType.Undefined,
        )
    }
}
