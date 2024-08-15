package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainingsList.api.di.TrainingsScreenDiApi
import ru.miem.psychoEvaluation.feature.trainingsList.impl.di.TrainingsListScreenComponent

@Module
class TrainingsListScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(TrainingsScreenDiApi::class)
    fun provideTrainingsListScreenApiProvider() = ApiProvider(TrainingsListScreenComponent.Companion::create)
}
