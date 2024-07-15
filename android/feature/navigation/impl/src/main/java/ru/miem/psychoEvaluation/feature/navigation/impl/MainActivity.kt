package ru.miem.psychoEvaluation.feature.navigation.impl

import android.app.Activity
import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.compose.setContent
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
import ru.miem.psychoEvaluation.feature.navigation.impl.ui.Navigation

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavHostController

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

    private companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}
