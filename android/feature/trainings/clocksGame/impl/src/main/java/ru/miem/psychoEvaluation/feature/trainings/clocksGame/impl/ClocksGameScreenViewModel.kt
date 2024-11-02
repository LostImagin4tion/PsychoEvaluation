package ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Environment
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
import ru.miem.psychoEvaluation.common.designSystem.utils.toString
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.BluetoothDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.bleDeviceInteractor.api.UsbDeviceInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.di.StatisticsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.networkApi.statistics.api.model.SendClocksGameStatisticsData
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.di.SettingsInteractorDiApi
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.core.di.impl.diApi
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ActionButtonClickFailed
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ActionButtonClickSuccessful
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ArrowJumped
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameInProgress
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameLoading
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameState
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.ClocksGameStatisticsState
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.HideIndicatorAndBrokenHeart
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.state.UiAction
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Suppress("MagicNumber")
class ClocksGameScreenViewModel(
    private val usbDeviceInteractor: UsbDeviceInteractor,
    private val bleDeviceInteractor: BluetoothDeviceInteractor,
) : ViewModel() {

    private val settingsInteractor by diApi(SettingsInteractorDiApi::settingsInteractor)
    private val statisticsInteractor by diApi(StatisticsInteractorDiApi::statisticsInteractor)

    private val _clocksGameState = MutableStateFlow<ClocksGameState>(defaultLoadingState)
    private val _sensorDeviceType = MutableStateFlow(SensorDeviceType.Unknown)

    private val allStress = mutableListOf<Int>()
    private var currentAction: UiAction? = null
    private var isActionButtonClicked = false

    private var fileOutputWriter: BufferedWriter? = null

    private val mutex = Mutex()

    val clocksGameState: StateFlow<ClocksGameState> = _clocksGameState
    val sensorDeviceType: StateFlow<SensorDeviceType> = _sensorDeviceType

    fun subscribeForSettingsChanges() {
        settingsInteractor.getCurrentSensorDeviceType()
            .onEach { _sensorDeviceType.emit(it) }
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
        when (_sensorDeviceType.value) {
            SensorDeviceType.Usb -> usbDeviceInteractor.disconnect()
            SensorDeviceType.Bluetooth -> bleDeviceInteractor.disconnect()
            SensorDeviceType.Unknown -> {}
        }
    }

    fun startTimerBeforeStart(context: Context) {
        viewModelScope.launch {
            coroutineScope {
                tickerFlow(defaultLoadingPeriod)
                    .onEach { delta ->
                        val state = _clocksGameState.value as? ClocksGameLoading

                        state?.let {
                            val newProgress = 1f - it.timeBeforeStart / defaultLoadingTimer
                            val newTime = it.timeBeforeStart - delta

                            val newState = it.copy(
                                timeBeforeStart = newTime,
                                progress = newProgress,
                            )

                            _clocksGameState.emit(newState)

                            check(newTime > 0.seconds)
                        }
                    }
                    .catch {
                        delay(100.milliseconds)
                        _clocksGameState.emit(defaultInProgressState)
                        startGame(context)
                        this@coroutineScope.cancel()
                    }
                    .launchIn(this@coroutineScope)
            }
        }
    }

    private fun startGame(context: Context) {
        setupFileInputStream(context)
        viewModelScope.launch {
            mutex.withLock {
                allStress.clear()
            }
            _clocksGameState.emit(
                defaultInProgressState.copy(
                    gameDate = Calendar.getInstance().time
                )
            )
        }

        viewModelScope.launch {
            var timeForArrowJump = Random.Default.nextInt(60..90).seconds

            coroutineScope {
                tickerFlow(defaultPeriod)
                    .runningReduce { accumulator, value ->
                        accumulator + value
                    }
                    .onEach { gameTime ->
                        _clocksGameState.run {
                            val clocksTimeDelta = if (gameTime >= timeForArrowJump) {
                                dispatchAction(ArrowJumped)

                                timeForArrowJump += Random.Default.nextInt(60..90).seconds

                                arrowJumpDelta
                            } else {
                                defaultPeriod
                            }

                            val state = _clocksGameState.value

                            if (state is ClocksGameStatisticsState) {
                                currentAction = null
                                isActionButtonClicked = false
                                this@coroutineScope.cancel()
                            }

                            (state as? ClocksGameInProgress)
                                ?.let {
                                    val newState = it.copy(
                                        clocksTime = it.clocksTime + clocksTimeDelta,
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

    fun restartGame(context: Context) {
        viewModelScope.launch {
            _clocksGameState.emit(defaultLoadingState)
        }
        startTimerBeforeStart(context)
    }

    fun closeStream() {
        fileOutputWriter?.let {
            it.flush()
            it.close()
            Timber.tag(TAG).i("Closed file output writer")
        }
        fileOutputWriter = null
    }

    private fun dispatchAction(action: UiAction) {
        currentAction = action
        viewModelScope.launch {
            when (action) {
                ArrowJumped -> {
                    _clocksGameState.run {
                        val state = value as? ClocksGameInProgress

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
                    _clocksGameState.run {
                        val state = value as? ClocksGameInProgress

                        state?.let {
                            val newState = it.copy(
                                successfulReactionCount = it.successfulReactionCount + 1,
                                reactionTimings = it.reactionTimings + action.reactionTiming,
                                currentIndicatorType = ClocksGameInProgress.IndicatorType.Success,
                            )
                            emit(newState)
                        }
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }

                is ActionButtonClickFailed -> {
                    isActionButtonClicked = false
                    _clocksGameState.run {
                        val state = value as? ClocksGameInProgress

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
                                currentIndicatorType = ClocksGameInProgress.IndicatorType.Failure,
                            )

                            emit(failedState)
                            startTimerForShowingBrokenHeartAndIndicatorButton()

                            if (newHeartsNumber == 0) {
                                val gameEndedState = failedState.toGameStatisticsState()
                                delay(defaultPeriodForHidingIndicatorAndBrokenHeart)
                                sendClocksGameStatistics(gameEndedState)
                                emit(gameEndedState)
                            }
                        }
                    }
                    startTimerForShowingBrokenHeartAndIndicatorButton()
                }

                HideIndicatorAndBrokenHeart -> changeIndicatorType(ClocksGameInProgress.IndicatorType.Undefined)
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

    private suspend fun changeIndicatorType(type: ClocksGameInProgress.IndicatorType) {
        _clocksGameState.run {
            val state = value as? ClocksGameInProgress
            state?.let {
                emit(it.copy(currentIndicatorType = type))
            }
        }
    }

    private fun sendClocksGameStatistics(state: ClocksGameStatisticsState) {
        viewModelScope.launch {
            val allStressCopy = buildList {
                mutex.withLock {
                    addAll(allStress)
                }
            }

            val data = SendClocksGameStatisticsData(
                gsrGame = allStressCopy,
                gameDuration = state.gameDuration.inWholeMilliseconds,
                gameLevel = 2,
                date = state.gameDate.formatted(),
                gameScore = state.score,
                reactionSpeed = state.reactionTimings
            )

            statisticsInteractor.sendClocksGameStatistics(data)
        }
    }

    private fun tickerFlow(period: Duration) = flow {
        while (true) {
            emit(period)
            delay(period)
        }
    }.cancellable()

    private suspend fun ClocksGameInProgress.toGameStatisticsState(): ClocksGameStatisticsState {
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

            ClocksGameStatisticsState(
                gameDate = gameDate,
                gameDuration = gameDuration,
                gameDurationString = gameDurationString,
                successPercent = successfulReactionCount.toFloat() / jumpCount,
                score = successfulReactionCount,
                averageReactionTimeString = averageReactionTime,
                reactionTimings = reactionTimings,
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
            fileOutputWriter?.write("$data\n")
            allStress.add(data)
        }

        _clocksGameState.run {
            val state = value as? ClocksGameInProgress

            state?.let {
                val newState = it.copy(
                    stressData = it.stressData + data
                )
                _clocksGameState.emit(newState)
            }
        }
    }

    private fun setupFileInputStream(context: Context) {
        try {
            closeStream()

            val datetime = Calendar.getInstance().time.toString("yyyy-MM-dd_HH:mm:ss")
            val filename = "clocks-psycho-$datetime.txt"

            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename)
            fileOutputWriter = file.bufferedWriter()

            Timber.tag(TAG).i("Created new file ${file.absolutePath}")
        } catch (e: IOException) {
            Timber.tag(TAG).e("Got IO error while writing data to file: $e ${e.message}")
        }
    }

    private companion object {
        val TAG: String = ClocksGameScreenViewModel::class.java.simpleName

        val defaultLoadingTimer = 3.seconds
        val defaultLoadingPeriod = 10.milliseconds
        val defaultPeriod = 100.milliseconds
        val arrowJumpDelta = 5.minutes
        val defaultTimeToClickActionButton = 5.seconds
        val defaultPeriodForHidingIndicatorAndBrokenHeart = 2.seconds

        val defaultLoadingState = ClocksGameLoading(
            timeBeforeStart = defaultLoadingTimer,
            progress = 1.0
        )
        val defaultInProgressState = ClocksGameInProgress(
            clocksTime = 0.seconds,
            gameDate = Calendar.getInstance().time,
            gameDuration = 0.seconds,
            gameDurationString = "00:00",
            heartsNumber = 3,
            jumpCount = 0,
            successfulReactionCount = 0,
            reactionTimings = emptyList(),
            stressData = emptyList(),
            currentIndicatorType = ClocksGameInProgress.IndicatorType.Undefined,
        )
    }
}
