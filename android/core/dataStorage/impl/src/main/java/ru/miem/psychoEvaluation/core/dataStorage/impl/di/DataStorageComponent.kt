package ru.miem.psychoEvaluation.core.dataStorage.impl.di

import dagger.BindsInstance
import dagger.Component
import dagger.Component.Builder
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStoreProvider
import ru.miem.psychoEvaluation.core.dataStorage.api.di.DataStorageDiApi

@Component(
    modules = [
        DataStorageModule::class
    ]
)
interface DataStorageComponent : DataStorageDiApi {

    @Component.Builder
    interface Builder {
        @BindsInstance fun dataStoreProvider(dataStoreProvider: DataStoreProvider): Builder
        fun build(): DataStorageComponent
    }

    companion object {
        fun create(
            dataStoreProvider: DataStoreProvider
        ): DataStorageDiApi = DaggerDataStorageComponent.builder()
            .dataStoreProvider(dataStoreProvider)
            .build()
    }
}