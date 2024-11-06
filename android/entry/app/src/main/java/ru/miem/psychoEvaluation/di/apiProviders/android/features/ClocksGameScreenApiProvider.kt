package ru.miem.psychoEvaluation.di.apiProviders.android.features

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.api.di.ClocksGameScreenDiApi
import ru.miem.psychoEvaluation.feature.trainings.clocksGame.impl.di.ClocksGameScreenComponent

@Module
class ClocksGameScreenApiProvider {

    @Provides
    @IntoMap
    @ApiKey(ClocksGameScreenDiApi::class)
    fun provideClocksGameScreenApiProvider() = ApiProvider(ClocksGameScreenComponent::create)
}
