package ru.miem.psychoEvaluation.core.dataStorage.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface DataStoreProvider {
    operator fun invoke(): DataStore<Preferences>
}
