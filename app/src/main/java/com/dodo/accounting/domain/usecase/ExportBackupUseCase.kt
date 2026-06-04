package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.repository.AccountingRepository
import javax.inject.Inject

class ExportBackupUseCase @Inject constructor(
    private val repository: AccountingRepository
) {
    suspend fun json(): String = repository.exportJson()
    suspend fun csv(): String = repository.exportCsv()
}
