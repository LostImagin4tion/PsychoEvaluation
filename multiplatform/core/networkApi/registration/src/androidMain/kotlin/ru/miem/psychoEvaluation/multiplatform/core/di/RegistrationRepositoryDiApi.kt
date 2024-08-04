package ru.miem.psychoEvaluation.multiplatform.core.di

import ru.miem.psychoEvaluation.core.di.api.DiApi
import ru.miem.psychoEvaluation.multiplatform.core.RegistrationRepository

interface RegistrationRepositoryDiApi : DiApi {
    val registrationRepository: RegistrationRepository
}
