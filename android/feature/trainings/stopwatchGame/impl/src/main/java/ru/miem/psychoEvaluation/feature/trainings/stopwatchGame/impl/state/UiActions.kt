package ru.miem.psychoEvaluation.feature.trainings.stopwatchGame.impl.state

sealed interface UiAction

data object ArrowJumped: UiAction

data object ActionButtonClickSuccessful: UiAction
data object ActionButtonClickFailed: UiAction

data object HideIndicatorAndBrokenHeart: UiAction
