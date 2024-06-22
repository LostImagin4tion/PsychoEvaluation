package ru.miem.psychoEvaluation.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStoreProvider
import ru.miem.psychoEvaluation.core.dataStorage.impl.di.DataStorageApiProvider
import ru.miem.psychoEvaluation.core.deviceApi.usbDeviceApi.impl.di.UsbDeviceRepositoryApiProvider
import ru.miem.psychoEvaluation.dataStore

@Module(
    includes = [
        DataStorageApiProvider::class,
        UsbDeviceRepositoryApiProvider::class,
    ]
)
interface CoreAggregationModule {

    companion object {

        @Provides
        fun provideDataStoreProvider(context: Context): DataStoreProvider {
            return object : DataStoreProvider {
                override fun invoke(): DataStore<Preferences> = context.dataStore
            }
        }
    }
}
