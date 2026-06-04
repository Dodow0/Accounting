package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.repository.AccountingRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: AccountingRepository
) {
    suspend operator fun invoke(draft: TransactionDraft): Long = repository.addTransaction(draft)
}
