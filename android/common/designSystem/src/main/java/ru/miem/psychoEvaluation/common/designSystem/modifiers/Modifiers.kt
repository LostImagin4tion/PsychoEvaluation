package ru.miem.psychoEvaluation.common.designSystem.modifiers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import ru.miem.psychoEvaluation.common.designSystem.theme.Dimensions

fun Modifier.screenPaddings() = this then Modifier
    .fillMaxSize()
    .padding(horizontal = Dimensions.primaryHorizontalPadding)
    .imePadding()
