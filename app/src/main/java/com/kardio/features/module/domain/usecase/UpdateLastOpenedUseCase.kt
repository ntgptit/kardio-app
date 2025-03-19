// features/module/domain/usecase/UpdateLastOpenedUseCase.kt
package com.kardio.features.module.domain.usecase

import com.kardio.core.base.ResultWrapper
import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.domain.repository.ModuleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateLastOpenedUseCase @Inject constructor(
    private val moduleRepository: ModuleRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(moduleId: String): ResultWrapper<Unit> =
        withContext(dispatcher) {
            moduleRepository.updateModuleLastOpened(moduleId)
        }
}

