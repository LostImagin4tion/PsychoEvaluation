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
import kotlinx.coroutines.flow.map
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
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.HideIndicatorAndBrokenHeart
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.IndicatorType
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.UiAction
import timber.log.Timber
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class StopwatchGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
): ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)

    private val _stopwatchGameState = MutableStateFlow(defaultState)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    private var isActionButtonClicked = false

    val stopwatchGameState: StateFlow<StopwatchGameState> = _stopwatchGameState
    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
            .launchIn(viewModelScope)
    }

    fun startGame() {
        Timber.tag(TAG).d("HELLO START GAME")
        var timeForArrowJump = Random.Default
            .nextInt(8..16)
            .seconds

        tickerFlow(defaultPeriod)
            .map { defaultPeriod }
            .runningReduce { accumulator, value ->
                accumulator + value
            }
            .onEach { gameTime ->
                Timber.tag(TAG).d("HELLO TIME $gameTime")
                _stopwatchGameState.run {
                    val stopWatchTimeDelta = if (gameTime >= timeForArrowJump) {
                        dispatchAction(ArrowJumped)

                        timeForArrowJump += Random.Default
                            .nextInt(8..16)
                            .seconds

                        defaultPeriod * 3
                    } else {
                        defaultPeriod
                    }

                    emit(
                        value.copy(
                            stopwatchTime = value.stopwatchTime + stopWatchTimeDelta.toDouble(DurationUnit.SECONDS).toFloat(),
                            timeString = createTimeString(gameTime)
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun clickActionButton() {
        isActionButtonClicked = true
    }

    private fun dispatchAction(action: UiAction) {
        Timber.tag(TAG).d("HELLO DISPATCH ACTION $action")
        viewModelScope.launch {
            when (action) {
                ArrowJumped -> startActionButtonTimer()
                ActionButtonClickSuccessful -> {
                    isActionButtonClicked = false
                    _stopwatchGameState.run {
                        emit(value.copy(currentIndicatorType = IndicatorType.Success))
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }
                ActionButtonClickFailed -> {
                    isActionButtonClicked = false
                    _stopwatchGameState.run {
                        val currentValue = value
                        emit(
                            currentValue.copy(
                                heartsNumber = currentValue.heartsNumber -1,
                                currentIndicatorType = IndicatorType.Failure,
                            )
                        )
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }
                HideIndicatorAndBrokenHeart -> changeIndicatorType(IndicatorType.Undefined)
            }
        }
    }

    private fun startActionButtonTimer() {
        Timber.tag(TAG).d("START ACTION BUTTON TIMER")
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultPeriodForTimer)
                    .scan(0.seconds) { time, _ ->
                        time + defaultPeriodForTimer
                    }
                    .onEach { timer ->
                        check(timer < defaultTimeToClickActionButton)

                        if (isActionButtonClicked) {
                            dispatchAction(ActionButtonClickSuccessful)
                            this@coroutineScope.cancel()
                        }
                    }
                    .catch {
                        dispatchAction(ActionButtonClickFailed)
                        this@coroutineScope.cancel()
                    }
                    .onEach {

                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private fun startTimerForShowingBrokenHeartAndIndicatorButton() {
        Timber.tag(TAG).d("HELLO START TIMER FOR SHOWING BROKEN HEART")
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultPeriod)
                    .scan(0.seconds) { time, _ ->
                        time + defaultPeriod
                    }
                    .onEach { timer ->
                        check(timer < defaultPeriodForHidingIndicatorAndBrokenHeart)
                    }
                    .catch {
                        dispatchAction(HideIndicatorAndBrokenHeart)
                        this@coroutineScope.cancel()
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private suspend fun changeIndicatorType(type: IndicatorType) {
        _stopwatchGameState.run {
            emit(value.copy(currentIndicatorType = type))
        }
    }

    private fun tickerFlow(period: Duration) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }.cancellable()

    private fun createTimeString(gameTime: Duration): String {
        val gameTimeMinutes = gameTime.inWholeMinutes.toInt()
        val gameTimeSeconds = gameTime.inWholeSeconds.toInt() - gameTimeMinutes * 60

        return when {
            gameTimeMinutes >= 10 && gameTimeSeconds >= 10 -> "$gameTimeMinutes:$gameTimeSeconds"
            gameTimeMinutes < 10 && gameTimeSeconds >= 10 -> "0$gameTimeMinutes:$gameTimeSeconds"
            gameTimeMinutes >= 10 && gameTimeSeconds < 10 -> "$gameTimeMinutes:0$gameTimeSeconds"
            else -> "0$gameTimeMinutes:0$gameTimeSeconds"
        }
    }

    private companion object {
        val TAG: String = StopwatchGameScreenViewModel::class.java.simpleName

        val defaultState = StopwatchGameState(
            stopwatchTime = 0f,
            timeString = "00:00",
            heartsNumber = 3,
            currentIndicatorType = IndicatorType.Undefined,
        )
        val defaultPeriod = 1.seconds
        val defaultPeriodForTimer = 0.1.seconds
        val defaultTimeToClickActionButton = 3.seconds
        val defaultPeriodForHidingIndicatorAndBrokenHeart = 2.seconds
    }
}