package ru.miem.psychoEvaluation.feature.userProfile.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.userProfile.api.UserProfileScreen
import ru.miem.psychoEvaluation.feature.userProfile.impl.UserProfileScreenImpl

@Module
interface UserProfileScreenModule {

    @Binds
    fun bindUserProfileScreen(impl: UserProfileScreenImpl): UserProfileScreen
}
