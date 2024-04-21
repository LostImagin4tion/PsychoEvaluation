package ru.miem.psychoEvaluation.common.designSystem.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
fun SystemBroadcastReceiver(
    isExported: Boolean = false,
    systemAction: String,
    onSystemEvent: (Intent) -> Unit,
    onDispose: () -> Unit = {},
) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                currentOnSystemEvent(intent)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val exportedFlag = if (isExported) {
                Context.RECEIVER_EXPORTED
            } else {
                Context.RECEIVER_NOT_EXPORTED
            }
            context.registerReceiver(broadcastReceiver, intentFilter, exportedFlag)
        } else {
            context.registerReceiver(broadcastReceiver, intentFilter)
        }

        onDispose {
            context.unregisterReceiver(broadcastReceiver)
            onDispose()
        }
    }
}
