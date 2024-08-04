package ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.api.RegistrationInteractor
import ru.miem.psychoEvaluation.common.interactors.networkApi.registration.impl.RegistrationInteractorImpl

@Module
interface RegistrationInteractorModule {

    @Binds
    fun bindRegistrationInteractor(impl: RegistrationInteractorImpl): RegistrationInteractor
}
