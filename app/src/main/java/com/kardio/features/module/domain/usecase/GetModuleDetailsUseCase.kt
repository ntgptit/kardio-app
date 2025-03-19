// features/module/domain/usecase/GetModuleDetailsUseCase.kt
package com.kardio.features.module.domain.usecase

import com.kardio.core.base.ResultWrapper
import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.domain.model.StudyModule
import com.kardio.features.module.domain.repository.ModuleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetModuleDetailsUseCase @Inject constructor(
    private val moduleRepository: ModuleRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(moduleId: String): Flow<ResultWrapper<StudyModule>> =
        withContext(dispatcher) {
            moduleRepository.getModuleById(moduleId)
        }
}

