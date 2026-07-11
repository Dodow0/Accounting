package com.dodo.accounting.domain.repository

import com.dodo.accounting.domain.model.BackupPreview

interface BackupRepository {
    suspend fun exportJson(): String
    suspend fun exportCsv(): String
    suspend fun previewImportJson(content: String): BackupPreview
    suspend fun importJson(content: String)
}
