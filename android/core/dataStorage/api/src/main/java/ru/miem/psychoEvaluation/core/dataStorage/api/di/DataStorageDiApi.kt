package ru.miem.psychoEvaluation.core.dataStorage.api.di

import ru.miem.psychoEvaluation.core.dataStorage.api.DataStorage
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface DataStorageDiApi : DiApi {
    val dataStorage: DataStorage
}
