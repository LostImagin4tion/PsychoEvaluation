package ru.miem.psychoEvaluation.common.designSystem.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier,
    loadingContent: (@Composable () -> Unit)? = null
) = Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    loadingContent?.invoke()
        ?: CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.size(48.dp)
        )
}
