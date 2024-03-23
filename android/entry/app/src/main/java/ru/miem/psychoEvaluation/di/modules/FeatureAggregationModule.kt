package ru.miem.psychoEvaluation.di.modules

import dagger.Module
import ru.miem.psychoEvaluation.feature.authorization.impl.di.AuthorizationScreenApiProvider
import ru.miem.psychoEvaluation.feature.registration.impl.di.RegistrationScreenApiProvider
import ru.miem.psychoEvaluation.feature.trainings.impl.di.TrainingsScreenApiProvider
import ru.miem.psychoEvaluation.feature.userProfile.impl.di.UserProfileScreenApiProvider

@Module(
    includes = [
        AuthorizationScreenApiProvider::class,
        RegistrationScreenApiProvider::class,
        UserProfileScreenApiProvider::class,
        TrainingsScreenApiProvider::class,
    ]
)
interface FeatureAggregationModule
