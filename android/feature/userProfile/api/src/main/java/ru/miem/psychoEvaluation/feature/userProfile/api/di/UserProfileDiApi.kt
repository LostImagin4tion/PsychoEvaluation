package ru.miem.psychoEvaluation.feature.userProfile.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen

interface UserProfileDiApi : DiApi {
    val userProfileScreen: UserProfileScreen
}
