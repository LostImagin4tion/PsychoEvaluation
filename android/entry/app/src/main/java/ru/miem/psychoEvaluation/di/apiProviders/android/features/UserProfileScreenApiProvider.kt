package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.userProfile.api.di.UserProfileDiApi
import ru.miem.psychoEvaluation.feature.userProfile.impl.di.UserProfileScreenComponent

@Module
class UserProfileScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(UserProfileDiApi::class)
    fun provideUserProfileScreenApiProvider() = ApiProvider(UserProfileScreenComponent.Companion::create)
}
