package ru.miem.psychoEvaluation.feature.navigation.impl

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.theme.PsychoEvaluationTheme
import ru.miem.psychoEvaluation.feature.navigation.api.data.Routes
import ru.miem.psychoEvaluation.feature.navigation.api.data.Screens
import ru.miem.psychoEvaluation.feature.navigation.impl.service.SerialListener
import ru.miem.psychoEvaluation.feature.navigation.impl.service.SerialService
import ru.miem.psychoEvaluation.feature.navigation.impl.service.SerialSocket
import ru.miem.psychoEvaluation.feature.navigation.impl.service.utils.TextUtil
import ru.miem.psychoEvaluation.feature.navigation.impl.ui.Navigation
import timber.log.Timber
import java.util.ArrayDeque

class MainActivity : AppCompatActivity(), ServiceConnection, SerialListener {

    private lateinit var navController: NavHostController

    var bluetoothAdapter: BluetoothAdapter? = null
    var deviceAddress: String? = null

    private var initialStart = true
    var isConnected = false

    private var pendingNewline = false
    private var newline = TextUtil.newline_crlf

    private var service: SerialService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            PsychoEvaluationTheme {
                SetupSystemBarsColors()

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            Snackbar(
                                snackbarData = it,
                                containerColor = MaterialTheme.colorScheme.inverseSurface,
                                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = MaterialTheme.shapes.small
                            )
                        }
                    },
                    bottomBar = { NavigationBar() },
                    content = {
                        Navigation(
                            snackbarHostState = snackbarHostState,
                            paddingValues = it,
                            navController = navController,
                            setupSystemBarColors = { SetupSystemBarsColors() }
                        )
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        Timber.tag("HELLO").e("HELLO ON NEW INTENT")
        if (intent?.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            navController.navigate(Routes.debugTraining)
        }
        super.onNewIntent(intent)
    }

    @Composable
    private fun SetupSystemBarsColors() {
        val view = LocalView.current
        if (!view.isInEditMode) {
            val currentWindow = (view.context as? Activity)?.window
                ?: error("Not in an activity - unable to get Window reference")

            val color = MaterialTheme.colorScheme.background.toArgb()

            val bottomBarColor = when (navController.currentDestination?.route) {
                in Routes.navigationBarDestinations -> MaterialTheme.colorScheme.primaryContainer.toArgb()
                else -> MaterialTheme.colorScheme.background.toArgb()
            }

            val isLightStatusBar = !isSystemInDarkTheme()

            SideEffect {
                currentWindow.statusBarColor = color
                currentWindow.navigationBarColor = bottomBarColor

                WindowCompat.getInsetsController(currentWindow, view)
                    .isAppearanceLightStatusBars = isLightStatusBar
            }
        }
    }

    @Composable
    private fun NavigationBar() {
        val screens = Screens.entries.toList()
        val routes = screens.map { it.route }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.hierarchy?.first()?.route

        // hide bottom bar for other screens
        if (currentRoute !in routes) return

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.height(70.dp),
        ) {
            NavigationBarItems(screens = screens, currentRoute = currentRoute)
        }
    }

    @Composable
    fun RowScope.NavigationBarItems(screens: List<Screens>, currentRoute: String?) {
        screens.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                label = {
                    LabelText(
                        textRes = it.nameRes,
                        isMedium = false
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(it.iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(vertical = 1.dp)
                    )
                },
                onClick = {
                    if (it.route != currentRoute) {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) { }
                        }
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .clip(CircleShape),
            )
        }
    }

    fun startSerialService(bluetoothAdapter: BluetoothAdapter, deviceAddress: String) {
        this.bluetoothAdapter = bluetoothAdapter
        this.deviceAddress = deviceAddress

        service?.attach(this)
            ?: run {
                Timber.tag("HELLO").d("ACTIVITY $this")
                this.bindService(
                    Intent(this, SerialService::class.java),
                    this,
                    Context.BIND_AUTO_CREATE
                )
            }
    }

    fun stopService() {
        service?.detach()
    }

    fun unbindService(context: Context) {
        context.unbindService(this)
    }

    fun connect(context: Context, bluetoothAdapter: BluetoothAdapter, deviceAddress: String) {
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        val socket = SerialSocket(context, device)
        service?.connect(socket)
    }

    fun disconnect() {
        service?.disconnect()
    }

    private fun receive(datas: ArrayDeque<ByteArray>) {
        val spn = SpannableStringBuilder()
        for (data in datas) {
            var msg = String(data)
            Timber.tag("HELLO").d("NEW MESSAGE $msg")

            if (newline == TextUtil.newline_crlf && msg.isNotEmpty()) {
                // don't show CR as ^M if directly before LF
                msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf)

                // special handling if CR and LF come in separate fragments
                if (pendingNewline && msg[0] == '\n') {
                    if (spn.length >= 2) {
                        spn.delete(spn.length - 2, spn.length)
                    }
                }
                pendingNewline = msg[msg.length - 1] == '\r'
            }
            Timber.tag("HELLO").d("NEW MESSAGE AFTER REPLACE $msg")
            spn.append(toCaretString(msg))
        }
        Timber.tag(TAG).d("HELLO NEW DATA $spn")
    }

    private fun toCaretString(string: CharSequence): CharSequence {
        var found = false

        for (i in string.indices) {
            if (string[i].code < 32 && string[i] != '\n') {
                found = true
                break
            }
        }
        if (!found) {
            return string
        }

        val sb = SpannableStringBuilder()

        for (pos in string.indices) {
            if (string[pos].code < 32 && string[pos] != '\n') {
                sb.append('^')
                sb.append(
                    (string[pos].code + 64).toChar()
                )
                sb.setSpan(
                    BackgroundColorSpan(caretBackground),
                    sb.length - 2,
                    sb.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                sb.append(string[pos])
            }
        }
        return sb
    }

    @ColorInt
    var caretBackground: Int = -0x99999a


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Timber.tag("HELLO").d("SERVICE CONNECTED")
        this.service = (service as SerialService.SerialBinder).service
            .apply {
                attach(this@MainActivity)
            }

        if (initialStart) {
            initialStart = false
            val bluetoothAdapter = this.bluetoothAdapter
            val deviceAddress = this.deviceAddress

            if (bluetoothAdapter != null && deviceAddress != null) {
                connect(this, bluetoothAdapter, deviceAddress)
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
    }

    override fun onSerialConnect() {
        isConnected = true

    }

    override fun onSerialConnectError(e: Exception?) {
        disconnect()
    }

    override fun onSerialRead(data: ByteArray?) {
        val datas = ArrayDeque<ByteArray>()
        datas.add(data)
        receive(datas)
    }

    override fun onSerialRead(datas: ArrayDeque<ByteArray>?) {
        datas?.let { receive(it) }
    }

    override fun onSerialIoError(e: Exception?) {
        disconnect()
    }

    private companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}
