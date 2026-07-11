package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.repository.PlanningRepository
import javax.inject.Inject

class EnsureSeedDataUseCase @Inject constructor(
    private val repository: PlanningRepository
) {
    suspend operator fun invoke() = repository.ensureSeedData()
}
