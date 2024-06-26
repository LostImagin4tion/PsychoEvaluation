package ru.miem.psychoEvaluation.feature.settings.impl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.miem.psychoEvaluation.common.designSystem.text.LabelText
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions
import ru.miem.psychoEvaluation.common.designSystem.theme.psychoGray
import ru.miem.psychoEvaluation.common.interactors.settingsInteractor.api.models.SensorDeviceType
import ru.miem.psychoEvaluation.feature.settings.impl.R

@Composable
fun SensorDeviceTypeRadioButton(
    currentType: SensorDeviceType,
    optionsList: List<SensorDeviceType>,
    onItemTapped: (SensorDeviceType) -> Unit = {},
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    LabelText(
        textRes = R.string.sensor_device_type_radio_button_text
    )

    Spacer(modifier = Modifier.height(Dimensions.commonSpacing))

    optionsList.forEach { deviceType ->
        val isSelected = deviceType == currentType
        val deviceTypeRes = when (deviceType) {
            SensorDeviceType.USB -> R.string.sensor_device_type_option_usb
            SensorDeviceType.BLUETOOTH -> R.string.sensor_device_type_option_bluetooth
            SensorDeviceType.UNKNOWN -> R.string.sensor_device_type_option_unknown
        }
        val deviceTypeText = stringResource(deviceTypeRes)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = isSelected,
                    onClick = {
                        onItemTapped(deviceType)
                    }
                )
        ) {
            RadioButton(
                selected = isSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledSelectedColor = psychoGray,
                    disabledUnselectedColor = psychoGray
                ),
                onClick = {
                    onItemTapped(deviceType)
                }
            )

            Spacer(modifier = Modifier.width(Dimensions.commonSpacing))

            LabelText(text = deviceTypeText)
        }
    }
}
