package ru.miem.psychoEvaluation.common.designSystem.system

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ru.miem.psychoEvaluation.common.designSystem.dialogs.SystemDialog
import ru.miem.psychoEvaluation.common.designSystem.utils.CommonDrawables
import ru.miem.psychoEvaluation.common.designSystem.utils.CommonStrings

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestBluetoothPermissionsIfNeeded(
    arePermissionsGranted: (Boolean) -> Unit = {},
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

    if (permissionsToRequest.allPermissionsGranted) {
        arePermissionsGranted(true)
    }
    else if (permissionsToRequest.shouldShowRationale) {
        SystemDialog(
            headerText = stringResource(CommonStrings.request_bluetooth_permission_rationale_header_text),
            descriptionText = stringResource(CommonStrings.request_bluetooth_permission_rationale_description_text),
            iconRes = CommonDrawables.bluetooth_icon,
            onConfirm = {
                permissionsToRequest.launchMultiplePermissionRequest()
            }
        )
    }
    else {
        SideEffect {
            permissionsToRequest.launchMultiplePermissionRequest()
        }
    }
}