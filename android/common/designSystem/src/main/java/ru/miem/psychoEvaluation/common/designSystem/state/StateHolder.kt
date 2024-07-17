package ru.miem.psychoEvaluation.common.designSystem.state

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import ru.miem.psychoEvaluation.common.designSystem.loading.FullScreenLoading
import ru.miem.psychoEvaluation.common.designSystem.utils.ErrorResult
import ru.miem.psychoEvaluation.common.designSystem.utils.FullScreenLoadingResult
import ru.miem.psychoEvaluation.common.designSystem.utils.Result
import ru.miem.psychoEvaluation.common.designSystem.utils.SuccessResult

@Composable
fun <T> StateHolder(
    state: Result<T>,
    fullScreenLoadingContent: (@Composable () -> Unit)? = null,
    successContent: (@Composable (state: SuccessResult<T>) -> Unit)? = null,
    errorContent: (@Composable (state: ErrorResult<T>) -> Unit)? = null,
) = Crossfade(
    targetState = state,
    label = "StateHolder"
) {
    when (it) {
        is FullScreenLoadingResult -> FullScreenLoading(loadingContent = fullScreenLoadingContent)
        is SuccessResult -> successContent?.invoke(it)
        is ErrorResult -> errorContent?.invoke(it)
        else -> {}
    }
}
