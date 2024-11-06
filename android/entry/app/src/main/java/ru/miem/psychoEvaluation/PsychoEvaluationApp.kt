package ru.miem.psychoEvaluation

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import ru.miem.psychoEvaluation.di.PsychoEvaluationAppComponent
import ru.miem.psychoEvaluation.di.initApis
import timber.log.Timber

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences_data_store")

class PsychoEvaluationApp : Application() {

    override fun onCreate() {
        initStrictMode()
        super.onCreate()

        initTimber()
        psychoEvaluationAppComponent = initApis(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDropBox()
                    .build()
            )

            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    // .detectDiskReads() // TODO Shared Preferences ruins, move to DataStore?
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyDropBox()
                    .penaltyLog()
                    .build()
            )
        }
    }

    companion object {
        lateinit var psychoEvaluationAppComponent: PsychoEvaluationAppComponent
    }
}
