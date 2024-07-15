package ru.miem.psychoEvaluation.common.designSystem.system

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun EnableBluetoothIfNeeded(
    onBluetoothEnabled: () -> Unit = {}
) {
    val context = LocalContext.current
    val bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
        .adapter

    if (!bluetoothAdapter.isEnabled) {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                onBluetoothEnabled()
            }
        }
        SideEffect {
            launcher.launch(enableBluetoothIntent)
        }
    } else {
        onBluetoothEnabled()
    }
}
