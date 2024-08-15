package ru.miem.psychoEvaluation.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStoreProvider
import ru.miem.psychoEvaluation.dataStore
import ru.miem.psychoEvaluation.di.apiProviders.android.core.BluetoothDeviceRepositoryApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.core.DataStorageApiProvider
import ru.miem.psychoEvaluation.di.apiProviders.android.core.UsbDeviceRepositoryApiProvider

@Module(
    includes = [
        DataStorageApiProvider::class,
        UsbDeviceRepositoryApiProvider::class,
        BluetoothDeviceRepositoryApiProvider::class,
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
