package ru.miem.psychoEvaluation.core.dataStorage.impl.di

import dagger.Binds
import dagger.Module
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorage
import ru.miem.psychoEvaluation.core.dataStorage.impl.DataStorageImpl

@Module
interface DataStorageModule {

    @Binds
    fun bindDataStorage(impl: DataStorageImpl): DataStorage
}
