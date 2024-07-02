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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.feature.bluetoothDeviceManager.impl.R

@Composable
fun BluetoothDeviceItem(
    deviceName: String? = null,
    hardwareAddress: String,
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
        painter = painterResource(ru.miem.psychoEvaluation.common.designSystem.R.drawable.bluetooth_icon),
        tint = MaterialTheme.colorScheme.onPrimary,
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )

    Spacer(modifier = Modifier.width(Dimensions.commonPadding))

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LabelText(
            text = deviceName ?: stringResource(R.string.device_name_default),
        )

        Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

        LabelText(
            text = hardwareAddress,
            isMedium = false
        )
    }
}