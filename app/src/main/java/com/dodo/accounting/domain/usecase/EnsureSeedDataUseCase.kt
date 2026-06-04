package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.repository.AccountingRepository
import javax.inject.Inject

class EnsureSeedDataUseCase @Inject constructor(
    private val repository: AccountingRepository
) {
    suspend operator fun invoke() = repository.ensureSeedData()
}
