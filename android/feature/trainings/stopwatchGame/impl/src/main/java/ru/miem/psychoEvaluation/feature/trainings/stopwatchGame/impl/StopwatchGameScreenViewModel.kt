package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.ActionButtonClickFailed
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.ActionButtonClickSuccessful
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.ArrowJumped
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.HideIndicatorAndBrokenHeart
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameInProgressState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameLoadingState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.StopwatchGameStatisticsState
import ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state.UiAction
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Suppress("MagicNumber")
class StopwatchGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)
    private val statisticsInteractor by diApi(StatisticsInteractorDiApi::statisticsInteractor)

    private val _stopwatchGameState = MutableStateFlow<StopwatchGameState>(defaultLoadingState)
    private val _stressData = MutableStateFlow(0)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    private val allStress = mutableListOf<Int>()
    private var currentAction: UiAction? = null
    private var isActionButtonClicked = false

    private val mutex = Mutex()

    val stopwatchGameState: StateFlow<StopwatchGameState> = _stopwatchGameState
    val stressData: StateFlow<Int> = _stressData
    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach {
                _sensorDeviceType.emit(it)
            }
            .launchIn(viewModelScope)
    }

    fun connectToUsbDevice(usbManager: UsbManager) {
        viewModelScope.launch {
            usbDeviceInteractor.getRawDeviceData(usbManager) {
                emitNewData(it)
            }
        }
    }

    fun retrieveDataFromBluetoothDevice(
        activity: Activity,
        bluetoothAdapter: BluetoothAdapter,
        bleDeviceHardwareAddress: String,
    ) {
        bleDeviceInteractor.connectToBluetoothDevice(
            activity = activity,
            bluetoothAdapter = bluetoothAdapter,
            deviceHardwareAddress = bleDeviceHardwareAddress,
        )

        viewModelScope.launch {
            bleDeviceInteractor.getRawDeviceData {
                emitNewData(it)
            }
        }
    }

    fun disconnect() {
        Timber.tag("HELLO disconnect")
        when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.disconnect()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.disconnect()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun startTimerBeforeStart() {
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultLoadingPeriod)
                    .onEach { delta ->
                        val state = _stopwatchGameState.value as? StopwatchGameLoadingState

                        state?.let {
                            val newProgress = 1f - it.timeBeforeStart / defaultLoadingTimer
                            val newTime = it.timeBeforeStart - delta

                            val newState = it.copy(
                                timeBeforeStart = newTime,
                                progress = newProgress,
                            )

                            _stopwatchGameState.emit(newState)

                            check(newTime > 0.seconds)
                        }
                    }
                    .catch {
                        delay(100.milliseconds)
                        _stopwatchGameState.emit(defaultInProgressState)
                        startGame()
                        this@coroutineScope.cancel()
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private fun startGame() {
        viewModelScope.launch {
            mutex.withLock {
                allStress.clear()
            }
            _stopwatchGameState.emit(
                defaultInProgressState.copy(
                    gameDate = Calendar.getInstance().time,
                )
            )
            var timeForArrowJump = Random.Default.nextInt(8..16).seconds

            coroutineScope {
                tickerFlow(defaultPeriod)
                    .runningReduce { accumulator, value ->
                        accumulator + value
                    }
                    .onEach { gameTime ->
                        _stopwatchGameState.run {
                            val stopwatchTimeDelta = if (gameTime >= timeForArrowJump) {
                                dispatchAction(ArrowJumped)

                                timeForArrowJump += Random.Default.nextInt(8..16).seconds

                                arrowJumpDelta
                            } else {
                                defaultPeriod
                            }

                            val state = _stopwatchGameState.value

                            if (state is StopwatchGameStatisticsState) {
                                currentAction = null
                                isActionButtonClicked = false
                                this@coroutineScope.cancel()
                            }

                            (state as? StopwatchGameInProgressState)
                                ?.let {
                                    val newState = it.copy(
                                        stopwatchTime = it.stopwatchTime + stopwatchTimeDelta,
                                        gameDuration = gameTime,
                                        gameDurationString = createTimeString(gameTime),
                                    )

                                    emit(newState)
                                }
                        }
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    fun clickActionButton() {
        if (currentAction is ArrowJumped) {
            isActionButtonClicked = true
        } else {
            dispatchAction(ActionButtonClickFailed(null))
        }
    }

    fun restartGame() {
        viewModelScope.launch {
            _stopwatchGameState.emit(defaultLoadingState)
        }
        startTimerBeforeStart()
    }

    private fun dispatchAction(action: UiAction) {
        currentAction = action
        viewModelScope.launch {
            when (action) {
                ArrowJumped -> {
                    _stopwatchGameState.run {
                        val state = value as? StopwatchGameInProgressState

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
                        val state = value as? StopwatchGameInProgressState

                        state?.let {
                            val newState = it.copy(
                                successfulReactionCount = it.successfulReactionCount + 1,
                                reactionTimings = it.reactionTimings + action.reactionTiming,
                                currentIndicatorType = StopwatchGameInProgressState.IndicatorType.Success,
                            )
                            emit(newState)
                        }
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }

                is ActionButtonClickFailed -> {
                    isActionButtonClicked = false
                    _stopwatchGameState.run {
                        val state = value as? StopwatchGameInProgressState

                        if (state != null) {
                            val newHeartsNumber = state.heartsNumber - 1
                            val failedState = state.copy(
                                heartsNumber = newHeartsNumber,
                                jumpCount = if (action.reactionTiming == null) {
                                    state.jumpCount + 1
                                } else {
                                    state.jumpCount
                                },
                                reactionTimings = action.reactionTiming
                                    ?.let { state.reactionTimings + it }
                                    ?: state.reactionTimings,
                                currentIndicatorType = StopwatchGameInProgressState.IndicatorType.Failure,
                            )

                            emit(failedState)
                            startTimerForShowingBrokenHeartAndIndicatorButton()

                            if (newHeartsNumber == 0) {
                                val gameEndedState = failedState.toGameStatisticsState()
                                delay(defaultPeriodForHidingIndicatorAndBrokenHeart)
                                sendStopwatchGameStatistics(gameEndedState)
                                emit(gameEndedState)
                            }
                        }
                    }
                }

                HideIndicatorAndBrokenHeart -> changeIndicatorType(StopwatchGameInProgressState.IndicatorType.Undefined)
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
                        } else if (time >= defaultTimeToClickActionButton) {
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

    private suspend fun changeIndicatorType(type: StopwatchGameInProgressState.IndicatorType) {
        _stopwatchGameState.run {
            val state = value as? StopwatchGameInProgressState
            state?.let {
                emit(it.copy(currentIndicatorType = type))
            }
        }
    }

    private suspend fun sendStopwatchGameStatistics(state: StopwatchGameStatisticsState) {
        val gsrBreathing = when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.gsrBreathing
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.gsrBreathing
            SensorDeviceType.Unknown -> emptyList()
        }

        viewModelScope.launch {
            val allStressCopy = buildList {
                mutex.withLock {
                    addAll(allStress)
                }
            }

            statisticsInteractor.sendClocksGameStatistics(
                gsrBreathing = gsrBreathing,
                gsrGame = allStressCopy,
                duration = state.gameDuration.inWholeSeconds.toInt(),
                date = state.gameDate.formatted(),
                level = 1,
                score = state.score
            )
        }
    }

    private fun tickerFlow(period: Duration) = flow {
        while (true) {
            emit(period)
            delay(period)
        }
    }.cancellable()

    private suspend fun StopwatchGameInProgressState.toGameStatisticsState(): StopwatchGameStatisticsState {
        return withContext(Dispatchers.Default) {
            val averageReactionTime = createTimeString(
                duration = reactionTimings
                    .average()
                    .takeIf { !it.isNaN() }
                    ?.milliseconds
                    ?: 0.milliseconds
            )
            val vigilanceDelta = reactionTimings.takeIf { it.isNotEmpty() }
                ?.reduce { accumulated, value -> accumulated - value }
                ?: 0L
            val concentrationDelta = stressData.takeIf { it.isNotEmpty() }
                ?.reduce { accumulated, value -> accumulated - value }
                ?: 0

            StopwatchGameStatisticsState(
                gameDate = gameDate,
                gameDuration = gameDuration,
                gameDurationString = gameDurationString,
                successPercent = successfulReactionCount.toFloat() / jumpCount,
                score = successfulReactionCount,
                averageReactionTimeString = averageReactionTime,
                vigilanceDelta = vigilanceDelta,
                concentrationDelta = concentrationDelta
            )
        }
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

    private fun Date.formatted(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(this)
    }

    private suspend fun emitNewData(data: Int) {
        mutex.withLock {
            allStress.add(data)
        }

        _stopwatchGameState.run {
            val state = value as? StopwatchGameInProgressState

            state?.let {
                val newState = it.copy(
                    stressData = it.stressData + data
                )
                _stopwatchGameState.emit(newState)
            }
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

        val defaultLoadingState = StopwatchGameLoadingState(
            timeBeforeStart = defaultLoadingTimer,
            progress = 1.0
        )
        val defaultInProgressState = StopwatchGameInProgressState(
            stopwatchTime = 0.seconds,
            gameDate = Calendar.getInstance().time,
            gameDuration = 0.seconds,
            gameDurationString = "00:00",
            heartsNumber = 3,
            jumpCount = 0,
            successfulReactionCount = 0,
            reactionTimings = emptyList(),
            stressData = emptyList(),
            currentIndicatorType = StopwatchGameInProgressState.IndicatorType.Undefined,
        )
    }
}
