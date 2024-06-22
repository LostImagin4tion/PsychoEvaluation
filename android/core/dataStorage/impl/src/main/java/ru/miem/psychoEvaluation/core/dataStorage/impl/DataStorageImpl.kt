package ru.miem.psychoEvaluation.core.dataStorage.impl

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorage
import ru.miem.psychoEvaluation.core.dataStorage.api.DataStoreProvider
import ru.miem.psychoEvaluation.core.dataStorage.api.internal.DataStorageKey
import javax.inject.Inject

class DataStorageImpl @Inject constructor(
    dataStoreProvider: DataStoreProvider
) : DataStorage {

    private val dataStore = dataStoreProvider()

    override fun <T> get(dataStorageKey: DataStorageKey<T>): Flow<T> {
        return dataStore.data.map {  prefs ->
            prefs[dataStorageKey.key] ?: dataStorageKey.default
        }
    }

    override suspend fun <T> set(dataStorageKey: DataStorageKey<T>, value: T) {
        dataStore.edit { prefs ->
            prefs[dataStorageKey.key] = value
        }
    }
}