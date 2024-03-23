package ru.miem.psychoEvaluation.feature.navigation.api.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.miem.psychoEvaluation.feature.navigation.api.R

enum class Screens(
    val route: String,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int
) {
    Trainings(Routes.trainings, R.string.bottom_bar_trainings_button, R.drawable.ic_training),
    Statistics(Routes.statistics, R.string.bottom_bar_statistics_button, R.drawable.ic_statistics),
    Profile(Routes.userProfile, R.string.bottom_bar_profile_button, R.drawable.ic_profile),
}