package ru.miem.psychoEvaluation.feature.userProfile.impl.di

import dagger.Component
import ru.miem.psychoEvaluation.feature.userProfile.api.di.UserProfileDiApi

@Component(
    modules = [
        UserProfileScreenModule::class,
    ],
)
interface UserProfileScreenComponent : UserProfileDiApi {
    companion object {
        fun create(): UserProfileDiApi = DaggerUserProfileScreenComponent.builder().build()
    }
}
