package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(draft: TransactionDraft): Long = repository.addTransaction(draft)
}
