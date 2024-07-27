package ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.di

import ru.miem.psychoEvaluation.common.interactors.networkApi.authorization.api.AuthorizationInteractor
import ru.miem.psychoEvaluation.core.di.api.DiApi

interface AuthorizationInteractorDiApi : DiApi {
    val authorizationInteractor: AuthorizationInteractor
}
