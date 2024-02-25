package ru.miem.psychoEvaluation.feature.authorization.api.di

import ru.miem.psychoEvaluation.core.di.api.Api
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen

interface AuthorizationApi : Api {
    val authorizationScreen: AuthorizationScreen
}
