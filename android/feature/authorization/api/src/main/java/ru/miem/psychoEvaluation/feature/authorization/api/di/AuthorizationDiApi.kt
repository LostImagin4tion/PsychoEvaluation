package ru.miem.psychoEvaluation.feature.authorization.api.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.feature.authorization.api.AuthorizationScreen

interface AuthorizationDiApi : DiApi {
    val authorizationScreen: AuthorizationScreen
}
