package com.dodo.accounting.ui.viewmodel.actions

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.domain.model.AccountRemovalAction
import com.dodo.accounting.domain.model.AccountRemovalResult
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.repository.AccountingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class ManagementActions(
    private val scope: CoroutineScope,
    private val repository: AccountingRepository,
    private val showMessage: (String) -> Unit
) {
    fun addAccount(name: String, initialBalance: String, iconName: String) {
        scope.launch {
            runCatching {
                require(name.isNotBlank()) { "账户名称不能为空" }
                repository.addAccount(
                    AccountEntity(
                        name = name.trim(),
                        initialBalanceCents = Money.requireMajorStrict(initialBalance).cents,
                        iconName = iconName.ifBlank { "account_balance_wallet" }
                    )
                )
            }.onSuccess {
                showMessage("资产账户已添加")
            }.onFailure {
                showMessage(it.message ?: "添加账户失败")
            }
        }
    }

    fun updateAccount(
        id: Long,
        name: String,
        initialBalance: String,
        iconName: String,
        colorArgb: Long
    ) {
        scope.launch {
            runCatching {
                require(name.isNotBlank()) { "账户名称不能为空" }
                repository.updateAccount(
                    id = id,
                    name = name.trim(),
                    initialBalanceCents = Money.requireMajorStrict(initialBalance).cents,
                    iconName = iconName,
                    colorArgb = colorArgb
                )
            }.onSuccess {
                showMessage("账户已更新")
            }.onFailure {
                showMessage(it.message ?: "更新账户失败")
            }
        }
    }

    fun archiveAccount(id: Long) {
        scope.launch {
            runCatching { repository.archiveAccount(id, true) }
                .onSuccess { showMessage(it.toAccountRemovalMessage()) }
                .onFailure { showMessage(it.message ?: "归档账户失败") }
        }
    }

    fun deleteAccount(id: Long) {
        scope.launch {
            runCatching { repository.deleteAccount(id) }
                .onSuccess { showMessage(it.toAccountRemovalMessage()) }
                .onFailure { showMessage(it.message ?: "移除账户失败") }
        }
    }

    fun restoreAccount(id: Long) {
        scope.launch {
            runCatching { repository.archiveAccount(id, false) }
                .onSuccess { showMessage(it.toAccountRemovalMessage()) }
                .onFailure { showMessage(it.message ?: "恢复账户失败") }
        }
    }

    fun reorderAccounts(ids: List<Long>) {
        scope.launch {
            runCatching { repository.reorderAccounts(ids) }
                .onFailure { showMessage(it.message ?: "账户排序失败") }
        }
    }

    fun addTag(name: String) {
        scope.launch {
            runCatching { repository.addTag(name) }
                .onSuccess { showMessage("标签已添加") }
                .onFailure { showMessage(it.message ?: "添加标签失败") }
        }
    }

    fun addCategory(
        name: String,
        kind: CategoryKind,
        iconName: String = if (kind == CategoryKind.EXPENSE) "receipt_long" else "work",
        colorArgb: Long = if (kind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A
    ) {
        scope.launch {
            runCatching {
                repository.addCategory(
                    CategoryEntity(
                        name = name,
                        kind = kind,
                        colorArgb = colorArgb,
                        iconName = iconName
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

    fun reorderCategories(ids: List<Long>) {
        scope.launch {
            runCatching { repository.reorderCategories(ids) }
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

    fun reorderTags(ids: List<Long>) {
        scope.launch {
            runCatching { repository.reorderTags(ids) }
                .onFailure { showMessage(it.message ?: "标签排序失败") }
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

internal fun AccountRemovalResult.toAccountRemovalMessage(): String {
    val base = when (action) {
        AccountRemovalAction.ARCHIVED -> "账户已归档"
        AccountRemovalAction.DELETED -> "账户已移除"
        AccountRemovalAction.RESTORED -> "账户已恢复"
        AccountRemovalAction.UNCHANGED -> "账户状态未变化"
    }
    val disabled = if (disabledRecurringRuleCount > 0) {
        "，已停用 $disabledRecurringRuleCount 条相关周期规则"
    } else {
        ""
    }
    return base + disabled
}
