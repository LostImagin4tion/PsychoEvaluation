package ru.miem.psychoEvaluation.common.designSystem.system

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestBluetoothPermissionsIfNeeded(
    arePermissionsGranted: (Boolean) -> Unit = {}
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        return
    }

    val permissionsToRequest = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
        ),
        onPermissionsResult = { results ->
            val allPermissionsGranted = results.all { it.value }
            arePermissionsGranted(allPermissionsGranted)
        }
    )

    if (permissionsToRequest.shouldShowRationale) {
        Log.d("HELLO", "SHOULD SHOW RATIONALE")
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        permissionsToRequest.launchMultiplePermissionRequest()
                    }
                ) {}
            }
        )
    } else {
        SideEffect {
            permissionsToRequest.launchMultiplePermissionRequest()
        }
    }
}