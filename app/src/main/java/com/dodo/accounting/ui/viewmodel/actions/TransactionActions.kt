package com.dodo.accounting.ui.viewmodel.actions

import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class TransactionActions(
    private val scope: CoroutineScope,
    private val repository: AccountingRepository,
    private val addTransaction: AddTransactionUseCase,
    private val editingTransaction: MutableStateFlow<TransactionWithDetails?>,
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
            draft = {
                TransactionDraft(
                    type = TransactionType.EXPENSE,
                    amountCents = Money.requireMajorStrict(amount).cents,
                    occurredAt = occurredAt,
                    accountId = accountId,
                    categoryId = categoryId,
                    merchant = merchant,
                    note = note,
                    tagIds = tagIds
                )
            },
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
            draft = {
                TransactionDraft(
                    type = TransactionType.INCOME,
                    amountCents = Money.requireMajorStrict(amount).cents,
                    occurredAt = occurredAt,
                    accountId = accountId,
                    categoryId = categoryId,
                    merchant = merchant,
                    note = note,
                    tagIds = tagIds
                )
            },
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
            draft = {
                TransactionDraft(
                    type = TransactionType.TRANSFER,
                    amountCents = Money.requireMajorStrict(amount).cents,
                    occurredAt = occurredAt,
                    fromAccountId = fromAccountId,
                    toAccountId = toAccountId,
                    note = note,
                    tagIds = tagIds
                )
            },
            successMessage = "转账已记录"
        )
    }

    fun startEditTransaction(transaction: TransactionWithDetails) {
        editingTransaction.value = transaction
    }

    fun cancelEditTransaction() {
        editingTransaction.value = null
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
        scope.launch {
            saveEditedTransactionAwait(
                transactionId = transactionId,
                type = type,
                amount = amount,
                accountId = accountId,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = tagIds,
                occurredAt = occurredAt
            )
        }
    }

    suspend fun addEntryTransaction(
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
    ): Result<Unit> {
        val successMessage = when (type) {
            TransactionType.EXPENSE -> "支出已记录"
            TransactionType.INCOME -> "收入已记录"
            TransactionType.TRANSFER -> "转账已记录"
            TransactionType.BALANCE_ADJUSTMENT -> "记录失败"
        }
        return runCatching {
            require(type != TransactionType.BALANCE_ADJUSTMENT) { "余额校正功能已移除" }
            addTransaction(
                createDraft(
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
            )
            Unit
        }
            .onSuccess { showMessage(successMessage) }
            .onFailure { showMessage(it.message ?: "记录失败") }
    }

    suspend fun saveEditedTransactionAwait(
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
    ): Result<Unit> {
        return runCatching {
            repository.updateTransaction(
                transactionId,
                createDraft(
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
            )
        }
            .onSuccess {
                editingTransaction.value = null
                showMessage("流水已更新")
            }
            .onFailure { showMessage(it.message ?: "更新失败") }
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

    fun clearTrash() {
        scope.launch {
            runCatching { repository.clearTrash() }
                .onSuccess { showMessage("回收站已清空") }
                .onFailure { showMessage(it.message ?: "清空回收站失败") }
        }
    }

    fun moveAllTransactionsToTrash() {
        scope.launch {
            runCatching { repository.softDeleteAllTransactions() }
                .onSuccess { count ->
                    showMessage(if (count > 0) "已将 $count 条流水移入回收站" else "没有可清空的流水")
                }
                .onFailure { showMessage(it.message ?: "清空流水失败") }
        }
    }

    private fun submitDraft(draft: () -> TransactionDraft, successMessage: String) {
        scope.launch {
            runCatching { addTransaction(draft()) }
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
            amountCents = Money.requireMajorStrict(amount).cents,
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
