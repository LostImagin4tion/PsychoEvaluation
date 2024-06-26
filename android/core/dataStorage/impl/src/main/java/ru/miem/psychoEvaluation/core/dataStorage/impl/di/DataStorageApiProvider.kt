package ru.miem.psychoEvaluation.core.dataStorage.impl.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStoreProvider
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi
import ru.miem.psychoEvaluation.core.di.impl.ApiKey
import ru.miem.psychoEvaluation.core.di.impl.ApiProvider

@Module
class DataStorageApiProvider {

    @Provides
    @IntoMap
    @ApiKey(DataStorageDiApi::class)
    fun provideDataStorageApiProvider(
        dataStoreProvider: DataStoreProvider
    ) = ApiProvider {
        DataStorageComponent.create(dataStoreProvider)
    }
}
