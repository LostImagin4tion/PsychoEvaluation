package ru.miem.psychoEvaluation.multiplatform.core

import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationRequest
import ru.miem.psychoEvaluation.multiplatform.core.models.RegistrationResponse

interface RegistrationRepository {
    suspend fun registration(request: RegistrationRequest): RegistrationResponse?
}
