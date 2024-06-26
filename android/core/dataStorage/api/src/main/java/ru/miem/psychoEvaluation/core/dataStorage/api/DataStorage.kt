package ru.miem.psychoEvaluation.core.dataStorage.api

import kotlinx.coroutines.flow.Flow
import ru.miem.psychoEvaluation.core.dataStorage.api.internal.DataStorageKey

interface DataStorage {
    operator fun <T> get(dataStorageKey: DataStorageKey<T>): Flow<T>
    suspend fun <T> set(dataStorageKey: DataStorageKey<T>, value: T)
}
