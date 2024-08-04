package ru.miem.psychoEvaluation.multiplatform.core.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.multiplatform.core.RegistrationRepository
import ru.miem.psychoEvaluation.multiplatform.core.RegistrationRepositoryImpl

@Module
interface RegistrationRepositoryModule {

    @Binds
    fun bindRegistrationRepository(impl: RegistrationRepositoryImpl): RegistrationRepository
}
