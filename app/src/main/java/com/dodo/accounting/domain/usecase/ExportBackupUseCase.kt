package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.repository.BackupRepository
import javax.inject.Inject

class ExportBackupUseCase @Inject constructor(
    private val backupRepository: BackupRepository
) {
    suspend fun json(): String = backupRepository.exportJson()
    suspend fun csv(): String = backupRepository.exportCsv()
}
