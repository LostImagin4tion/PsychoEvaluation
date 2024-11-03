package ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.utils.CommonDrawables
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.R
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.state.BluetoothDeviceConnectionStatus

@Composable
fun BluetoothDeviceItem(
    deviceName: String? = null,
    hardwareAddress: String,
    connectionStatus: BluetoothDeviceConnectionStatus = BluetoothDeviceConnectionStatus.Unknown,
    onClick: () -> Unit = {},
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .clip(Dimensions.rippleShape)
        .clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple()
        )
        .padding(Dimensions.ripplePadding)
        .fillMaxWidth()
) {
    Icon(
        painter = painterResource(CommonDrawables.bluetooth_icon),
        tint = MaterialTheme.colorScheme.onPrimary,
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )

    Spacer(modifier = Modifier.width(Dimensions.commonPadding))

    Column {
        LabelText(
            text = deviceName ?: stringResource(R.string.device_name_default),
        )

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        LabelText(
            text = hardwareAddress,
            isMedium = false
        )
    }

    Spacer(modifier = Modifier.weight(1f))

    when (connectionStatus) {
        BluetoothDeviceConnectionStatus.Unknown -> {}
        BluetoothDeviceConnectionStatus.InProgress -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp,
                strokeCap = StrokeCap.Round,
                modifier = Modifier.size(28.dp),
            )
        }
        BluetoothDeviceConnectionStatus.Connected -> {
            Icon(
                painter = painterResource(R.drawable.ready_circle),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}
