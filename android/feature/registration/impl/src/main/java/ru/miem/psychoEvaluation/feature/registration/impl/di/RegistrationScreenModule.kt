package ru.miem.psychoEvaluation.feature.registration.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.feature.registration.api.RegistrationScreen
import ru.miem.psychoEvaluation.feature.registration.impl.RegistrationScreenImpl

@Module
interface RegistrationScreenModule {

    @Binds
    fun bindAuthorizationScreen(impl: RegistrationScreenImpl): RegistrationScreen
}
