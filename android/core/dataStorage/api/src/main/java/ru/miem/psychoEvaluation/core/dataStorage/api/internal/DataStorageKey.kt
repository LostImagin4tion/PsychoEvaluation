package ru.miem.psychoEvaluation.core.dataStorage.api.internal

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

sealed class DataStorageKey<T> private constructor(
    val key: Preferences.Key<T>,
    val default: T
) {
    class DataStorageKeyBoolean internal constructor(
        name: String,
        default: Boolean = false
    ) : DataStorageKey<Boolean>(booleanPreferencesKey(name), default)

    class DataStorageKeyString internal constructor(
        name: String,
        default: String = ""
    ) : DataStorageKey<String>(stringPreferencesKey(name), default)

    class DataStorageKeyDouble internal constructor(
        name: String,
        default: Double = 0.0
    ) : DataStorageKey<Double>(doublePreferencesKey(name), default)

    class DataStorageKeyInt internal constructor(
        name: String,
        default: Int = 0
    ) : DataStorageKey<Int>(intPreferencesKey(name), default)
}
