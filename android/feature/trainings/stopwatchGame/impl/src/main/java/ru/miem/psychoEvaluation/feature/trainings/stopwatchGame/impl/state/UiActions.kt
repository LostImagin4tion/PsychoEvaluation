package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state

sealed interface UiAction

data object ArrowJumped : UiAction

data class ActionButtonClickSuccessful(
    val reactionTiming: Long,
) : UiAction

data class ActionButtonClickFailed(
    val reactionTiming: Long?,
) : UiAction

data object HideIndicatorAndBrokenHeart : UiAction
