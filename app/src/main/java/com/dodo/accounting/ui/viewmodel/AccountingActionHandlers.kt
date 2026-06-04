package com.dodo.accounting.ui.viewmodel

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.usecase.AddTransactionUseCase
import com.dodo.accounting.domain.usecase.ExportBackupUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class TransactionActions(
    private val scope: CoroutineScope,
    private val repository: AccountingRepository,
    private val addTransaction: AddTransactionUseCase,
    private val localState: MutableStateFlow<AccountingUiState>,
    private val showMessage: (String) -> Unit
) {
    fun addExpense(
        amount: String,
        accountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.EXPENSE,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                accountId = accountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "支出已记录"
        )
    }

    fun addIncome(
        amount: String,
        accountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.INCOME,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                accountId = accountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "收入已记录"
        )
    }

    fun addTransfer(
        amount: String,
        fromAccountId: Long?,
        toAccountId: Long?,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.TRANSFER,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "转账已记录"
        )
    }

    fun addBalanceAdjustment(
        amount: String,
        accountId: Long?,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.BALANCE_ADJUSTMENT,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                accountId = accountId,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "余额校正已记录"
        )
    }

    fun startEditTransaction(transaction: TransactionWithDetails) {
        localState.update { it.copy(editingTransaction = transaction) }
    }

    fun cancelEditTransaction() {
        localState.update { it.copy(editingTransaction = null) }
    }

    fun saveEditedTransaction(
        transactionId: Long,
        type: TransactionType,
        amount: String,
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long
    ) {
        val draft = createDraft(
            type = type,
            amount = amount,
            occurredAt = occurredAt,
            accountId = accountId,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            categoryId = categoryId,
            merchant = merchant,
            note = note,
            tagIds = tagIds
        )
        scope.launch {
            runCatching { repository.updateTransaction(transactionId, draft) }
                .onSuccess {
                    localState.update { state -> state.copy(editingTransaction = null) }
                    showMessage("流水已更新")
                }
                .onFailure { showMessage(it.message ?: "更新失败") }
        }
    }

    fun deleteTransaction(transactionId: Long) {
        scope.launch {
            runCatching { repository.softDeleteTransaction(transactionId) }
                .onSuccess { showMessage("已移入回收站") }
                .onFailure { showMessage(it.message ?: "删除失败") }
        }
    }

    fun restoreTransaction(transactionId: Long) {
        scope.launch {
            runCatching { repository.restoreTransaction(transactionId) }
                .onSuccess { showMessage("已恢复") }
                .onFailure { showMessage(it.message ?: "恢复失败") }
        }
    }

    fun permanentlyDeleteTransaction(transactionId: Long) {
        scope.launch {
            runCatching { repository.permanentlyDeleteTransaction(transactionId) }
                .onSuccess { showMessage("已彻底删除") }
                .onFailure { showMessage(it.message ?: "彻底删除失败") }
        }
    }

    private fun submitDraft(draft: TransactionDraft, successMessage: String) {
        scope.launch {
            runCatching { addTransaction(draft) }
                .onSuccess { showMessage(successMessage) }
                .onFailure { showMessage(it.message ?: "记录失败") }
        }
    }

    private fun createDraft(
        type: TransactionType,
        amount: String,
        occurredAt: Long,
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long>
    ): TransactionDraft {
        return TransactionDraft(
            type = type,
            amountCents = Money.fromMajor(amount).cents,
            occurredAt = occurredAt,
            accountId = if (type == TransactionType.TRANSFER) null else accountId,
            fromAccountId = if (type == TransactionType.TRANSFER) fromAccountId else null,
            toAccountId = if (type == TransactionType.TRANSFER) toAccountId else null,
            categoryId = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) categoryId else null,
            merchant = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) merchant else "",
            note = note,
            tagIds = tagIds
        )
    }
}

internal class PlanningActions(
    private val scope: CoroutineScope,
    private val repository: AccountingRepository,
    private val showMessage: (String) -> Unit
) {
    fun setMonthlyBudget(amount: String) {
        scope.launch {
            runCatching { repository.setMonthlyBudget(Money.fromMajor(amount).cents) }
                .onSuccess { showMessage("月度预算已更新") }
                .onFailure { showMessage(it.message ?: "预算设置失败") }
        }
    }

    fun setCategoryBudget(category: CategoryEntity, amount: String) {
        scope.launch {
            runCatching { repository.setCategoryBudget(category.id, category.name, Money.fromMajor(amount).cents) }
                .onSuccess { showMessage("${category.name} 预算已更新") }
                .onFailure { showMessage(it.message ?: "分类预算设置失败") }
        }
    }

    fun addMonthlyRecurringRule(
        name: String,
        type: TransactionType,
        amount: String,
        occurredAt: Long = System.currentTimeMillis(),
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String
    ) {
        scope.launch {
            runCatching {
                repository.addRecurringRule(
                    RecurringRuleEntity(
                        name = name.trim(),
                        transactionType = type,
                        amountCents = Money.fromMajor(amount).cents,
                        accountId = if (type == TransactionType.TRANSFER) null else accountId,
                        fromAccountId = if (type == TransactionType.TRANSFER) fromAccountId else null,
                        toAccountId = if (type == TransactionType.TRANSFER) toAccountId else null,
                        categoryId = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) categoryId else null,
                        merchant = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) merchant else "",
                        note = note,
                        intervalMonths = 1,
                        nextRunAt = occurredAt
                    )
                )
                repository.generateDueRecurringTransactions()
            }.onSuccess { generated ->
                showMessage(if (generated > 0) "周期规则已添加，已生成 $generated 条账单" else "周期规则已添加")
            }.onFailure {
                showMessage(it.message ?: "添加周期规则失败")
            }
        }
    }

    fun runDueRecurringRules() {
        scope.launch {
            runCatching { repository.generateDueRecurringTransactions() }
                .onSuccess { showMessage("已生成 $it 条到期账单") }
                .onFailure { showMessage(it.message ?: "生成周期账单失败") }
        }
    }

    fun setRecurringRuleEnabled(id: Long, enabled: Boolean) {
        scope.launch {
            runCatching { repository.setRecurringRuleEnabled(id, enabled) }
                .onSuccess { showMessage(if (enabled) "周期规则已启用" else "周期规则已停用") }
                .onFailure { showMessage(it.message ?: "更新周期规则失败") }
        }
    }

    fun deleteRecurringRule(id: Long) {
        scope.launch {
            runCatching { repository.deleteRecurringRule(id) }
                .onSuccess { showMessage("周期规则已删除") }
                .onFailure { showMessage(it.message ?: "删除周期规则失败") }
        }
    }
}

internal class ManagementActions(
    private val scope: CoroutineScope,
    private val repository: AccountingRepository,
    private val showMessage: (String) -> Unit
) {
    fun addAccount(name: String, type: AccountType, initialBalance: String) {
        scope.launch {
            runCatching {
                require(name.isNotBlank()) { "账户名称不能为空" }
                repository.addAccount(
                    AccountEntity(
                        name = name.trim(),
                        type = type,
                        initialBalanceCents = Money.fromMajor(initialBalance).cents,
                        sortOrder = System.currentTimeMillis().toInt()
                    )
                )
            }.onSuccess {
                showMessage("资产账户已添加")
            }.onFailure {
                showMessage(it.message ?: "添加账户失败")
            }
        }
    }

    fun addTag(name: String) {
        scope.launch {
            runCatching { repository.addTag(name) }
                .onSuccess { showMessage("标签已添加") }
                .onFailure { showMessage(it.message ?: "添加标签失败") }
        }
    }

    fun addCategory(name: String, kind: CategoryKind) {
        scope.launch {
            runCatching {
                repository.addCategory(
                    CategoryEntity(
                        name = name,
                        kind = kind,
                        colorArgb = if (kind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A,
                        iconName = if (kind == CategoryKind.EXPENSE) "receipt_long" else "work",
                        sortOrder = System.currentTimeMillis().toInt()
                    )
                )
            }
                .onSuccess { showMessage("分类已添加") }
                .onFailure { showMessage(it.message ?: "添加分类失败") }
        }
    }

    fun renameCategory(id: Long, name: String) {
        scope.launch {
            runCatching { repository.renameCategory(id, name) }
                .onSuccess { showMessage("分类已更新") }
                .onFailure { showMessage(it.message ?: "更新分类失败") }
        }
    }

    fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) {
        scope.launch {
            runCatching { repository.updateCategory(id, name, iconName, colorArgb) }
                .onSuccess { showMessage("分类已更新") }
                .onFailure { showMessage(it.message ?: "更新分类失败") }
        }
    }

    fun moveCategory(id: Long, direction: Int) {
        scope.launch {
            runCatching { repository.moveCategory(id, direction) }
                .onFailure { showMessage(it.message ?: "分类排序失败") }
        }
    }

    fun deleteCategory(id: Long) {
        scope.launch {
            runCatching { repository.deleteCategory(id) }
                .onSuccess { showMessage("分类已删除") }
                .onFailure { showMessage(it.message ?: "删除分类失败") }
        }
    }

    fun renameTag(id: Long, name: String) {
        scope.launch {
            runCatching { repository.renameTag(id, name) }
                .onSuccess { showMessage("标签已更新") }
                .onFailure { showMessage(it.message ?: "更新标签失败") }
        }
    }

    fun deleteTag(id: Long) {
        scope.launch {
            runCatching { repository.deleteTag(id) }
                .onSuccess { showMessage("标签已删除") }
                .onFailure { showMessage(it.message ?: "删除标签失败") }
        }
    }
}

internal class BackupActions(
    private val scope: CoroutineScope,
    private val repository: AccountingRepository,
    private val exportBackup: ExportBackupUseCase,
    private val localState: MutableStateFlow<AccountingUiState>,
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

    fun importJson(content: String) {
        scope.launch {
            runCatching { repository.importJson(content) }
                .onSuccess {
                    localState.update { state -> state.copy(exportPreview = "", exportContent = "") }
                    showMessage("JSON 备份已导入")
                }
                .onFailure { showMessage(it.message ?: "导入失败") }
        }
    }
}
