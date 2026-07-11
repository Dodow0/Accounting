package com.dodo.accounting.ui.viewmodel.actions

import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.repository.BackupRepository
import com.dodo.accounting.domain.usecase.ExportBackupUseCase
import com.dodo.accounting.ui.viewmodel.ExportFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal data class BackupUiLocalState(
    val exportPreview: String = "",
    val exportContent: String = "",
    val exportFormat: ExportFormat = ExportFormat.JSON,
    val pendingImportPreview: BackupPreview? = null,
    val pendingImportContent: String = ""
)

internal class BackupActions(
    private val scope: CoroutineScope,
    private val backupRepository: BackupRepository,
    private val exportBackup: ExportBackupUseCase,
    private val localState: MutableStateFlow<BackupUiLocalState>,
    private val showMessage: (String) -> Unit
) {
    fun export(format: ExportFormat) {
        scope.launch {
            localState.update { it.copy(exportFormat = format) }
            runCatching {
                when (format) {
                    ExportFormat.JSON -> exportBackup.json()
                    ExportFormat.CSV -> exportBackup.csv()
                }
            }.onSuccess { content ->
                localState.update {
                    it.copy(
                        exportPreview = content.take(12_000),
                        exportContent = content,
                        exportFormat = format
                    )
                }
                showMessage("${format.name} 已生成预览")
            }.onFailure {
                showMessage(it.message ?: "导出失败")
            }
        }
    }

    fun previewImportJson(content: String) {
        scope.launch {
            runCatching { backupRepository.previewImportJson(content) }
                .onSuccess { preview ->
                    localState.update { state ->
                        state.copy(
                            pendingImportPreview = preview,
                            pendingImportContent = content
                        )
                    }
                    showMessage("JSON 备份已读取，请确认导入")
                }
                .onFailure { showMessage(it.message ?: "读取备份失败") }
        }
    }

    fun confirmImportJson() {
        scope.launch {
            val content = localState.value.pendingImportContent
            runCatching {
                require(content.isNotBlank()) { "请先选择 JSON 备份" }
                backupRepository.importJson(content)
            }
                .onSuccess {
                    localState.update { state ->
                        state.copy(
                            exportPreview = "",
                            exportContent = "",
                            pendingImportPreview = null,
                            pendingImportContent = ""
                        )
                    }
                    showMessage("JSON 备份已导入")
                }
                .onFailure { showMessage(it.message ?: "导入失败") }
        }
    }

    fun cancelImportJson() {
        localState.update {
            it.copy(
                pendingImportPreview = null,
                pendingImportContent = ""
            )
        }
    }
}
