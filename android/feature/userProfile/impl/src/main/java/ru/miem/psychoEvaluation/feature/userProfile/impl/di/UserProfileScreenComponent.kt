package ru.miem.psychoEvaluation.feature.userProfile.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.userProfile.api.di.UserProfileApi

@Component(
    modules = [
        UserProfileScreenModule::class,
    ],
)
interface UserProfileScreenComponent: UserProfileApi {
    companion object {
        fun create(): UserProfileApi = DaggerUserProfileScreenComponent.builder().build()
    }
}