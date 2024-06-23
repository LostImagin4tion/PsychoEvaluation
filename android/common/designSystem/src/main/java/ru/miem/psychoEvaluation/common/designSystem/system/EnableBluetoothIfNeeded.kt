package ru.miem.psychoEvaluation.common.designSystem.system

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@Composable
fun EnableBluetoothIfNeeded(bluetoothAdapter: BluetoothAdapter) {
    Log.d("HELLO", "BLUETOOTH ENABLED ? ${bluetoothAdapter.isEnabled}")
    if (!bluetoothAdapter.isEnabled) {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {}
        SideEffect {
            launcher.launch(enableBluetoothIntent)
        }
    }
}