package ru.miem.psychoEvaluation.di

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.miem.psychoEvaluation.core.di.impl.ApiRegistry
import ru.miem.psychoEvaluation.core.di.impl.ApiResolver
import ru.miem.psychoEvaluation.di.modules.CommonAggregationModule
import ru.miem.psychoEvaluation.di.modules.CoreAggregationModule
import ru.miem.psychoEvaluation.di.modules.FeatureAggregationModule
import ru.miem.psychoEvaluation.di.modules.MultiplatformCoreModule

@Component(
    modules = [
        CoreAggregationModule::class,
        CommonAggregationModule::class,
        FeatureAggregationModule::class,

        MultiplatformCoreModule::class,
    ]
)
interface PsychoEvaluationAppComponent {

    val apiResolver: ApiResolver

    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder

        @BindsInstance fun context(context: Context): Builder
        fun build(): PsychoEvaluationAppComponent
    }
}

/**
 * Initialization of application's **DI graph**
 */
fun initApis(application: Application): PsychoEvaluationAppComponent = DaggerPsychoEvaluationAppComponent
    .builder()
    .application(application)
    .context(application)
    .build()
    .also {
        ApiRegistry.init(it.apiResolver)
    }
