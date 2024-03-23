package ru.miem.psychoEvaluation.feature.userProfile.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen

interface UserProfileApi : Api {
    val userProfileScreen: UserProfileScreen
}