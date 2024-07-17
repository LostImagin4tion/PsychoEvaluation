package ru.miem.psychoEvaluation.multiplatform.core.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.multiplatform.core.AuthorizationRepository

interface AuthorizationRepositoryDiApi : DiApi {
    val authorizationRepository: AuthorizationRepository
}