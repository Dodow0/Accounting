package com.dodo.accounting.data.repository.parts

import androidx.room.withTransaction
import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.local.SeedData
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.BudgetPeriod
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionSource
import com.dodo.accounting.data.local.entity.TransactionTagCrossRef
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.PeriodSummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.AccountRemovalAction
import com.dodo.accounting.domain.model.AccountRemovalResult
import com.dodo.accounting.domain.model.RecurringGenerationResult
import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.model.TransactionRules
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.flow.Flow

internal class CatalogRepositoryOps(
    private val database: AccountingDatabase
) {
    private val categoryDao = database.categoryDao()
    private val tagDao = database.tagDao()
    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()
    private val recurringRuleDao = database.recurringRuleDao()

    fun observeCategories(): Flow<List<CategoryEntity>> = categoryDao.observeCategories()
    fun observeCategories(kind: CategoryKind): Flow<List<CategoryEntity>> = categoryDao.observeCategories(kind)
    fun observeTags(): Flow<List<TagEntity>> = tagDao.observeTags()

    suspend fun addCategory(category: CategoryEntity): Long = database.withTransaction {
        val trimmed = category.name.trim()
        require(trimmed.isNotBlank()) { "分类名称不能为空" }
        val existing = categoryDao.getActiveCategoryByName(trimmed, category.kind)
        require(existing == null) { "分类已存在" }
        val nextSortOrder = (categoryDao.getActiveCategories(category.kind).maxOfOrNull { it.sortOrder } ?: -1) + 1
        categoryDao.insert(
            category.copy(
                name = trimmed,
                sortOrder = nextSortOrder,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun renameCategory(id: Long, name: String) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "分类名称不能为空" }
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        val sameName = categoryDao.getActiveCategoryByName(trimmed, existing.kind)
        require(sameName == null || sameName.id == id) { "分类名称已存在" }
        categoryDao.update(existing.copy(name = trimmed, updatedAt = System.currentTimeMillis()))
    }

    suspend fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "分类名称不能为空" }
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        val sameName = categoryDao.getActiveCategoryByName(trimmed, existing.kind)
        require(sameName == null || sameName.id == id) { "分类名称已存在" }
        categoryDao.update(
            existing.copy(
                name = trimmed,
                iconName = iconName.ifBlank { existing.iconName },
                colorArgb = colorArgb,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun moveCategory(id: Long, direction: Int) = database.withTransaction {
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        val categories = categoryDao.getActiveCategories(existing.kind)
        val currentIndex = categories.indexOfFirst { it.id == id }
        if (currentIndex == -1) return@withTransaction

        val targetIndex = (currentIndex + direction.coerceIn(-1, 1))
            .coerceIn(0, categories.lastIndex)
        if (targetIndex == currentIndex) return@withTransaction

        val target = categories[targetIndex]
        val now = System.currentTimeMillis()
        categoryDao.update(existing.copy(sortOrder = target.sortOrder, updatedAt = now))
        categoryDao.update(target.copy(sortOrder = existing.sortOrder, updatedAt = now))
    }

    suspend fun reorderCategories(ids: List<Long>) = database.withTransaction {
        if (ids.isEmpty()) return@withTransaction
        val first = categoryDao.getCategoryById(ids.first()) ?: error("分类不存在")
        val categories = categoryDao.getActiveCategories(first.kind)
        val requestedIds = ids.distinct()
        val activeIds = categories.map { it.id }
        require(requestedIds.size == ids.size && requestedIds.toSet() == activeIds.toSet()) { "分类排序数据已变化，请重试" }

        val byId = categories.associateBy { it.id }
        val now = System.currentTimeMillis()
        requestedIds.forEachIndexed { index, categoryId ->
            val category = byId.getValue(categoryId)
            if (category.sortOrder != index) {
                categoryDao.update(category.copy(sortOrder = index, updatedAt = now))
            }
        }
    }

    suspend fun deleteCategory(id: Long) = database.withTransaction {
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        if (existing.deletedAt != null) return@withTransaction
        val now = System.currentTimeMillis()
        transactionDao.clearCategoryReferences(id, now)
        budgetDao.archiveBudgetsForCategory(id, now)
        recurringRuleDao.clearCategoryReferences(id, now)
        categoryDao.softDelete(id, now)
    }

    suspend fun addTag(name: String): Long = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "标签名称不能为空" }
        val existing = tagDao.getTagByName(trimmed)
        val nextSortOrder = (tagDao.getMaxActiveSortOrder() ?: -1) + 1
        when {
            existing == null -> tagDao.insert(TagEntity(name = trimmed, sortOrder = nextSortOrder))
            existing.deletedAt != null -> {
                tagDao.update(
                    existing.copy(
                        sortOrder = nextSortOrder,
                        deletedAt = null,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                existing.id
            }
            else -> error("标签已存在")
        }
    }

    suspend fun renameTag(id: Long, name: String) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "标签名称不能为空" }
        val existing = tagDao.getTagById(id) ?: error("标签不存在")
        val sameName = tagDao.getTagByName(trimmed)
        require(sameName == null || sameName.id == id) { "标签名称已存在" }
        tagDao.update(existing.copy(name = trimmed, updatedAt = System.currentTimeMillis()))
    }

    suspend fun reorderTags(ids: List<Long>) = database.withTransaction {
        val tags = tagDao.getTagsSnapshot().filter { it.deletedAt == null }
        val requestedIds = ids.distinct()
        val activeIds = tags.map { it.id }
        require(requestedIds.size == ids.size && requestedIds.toSet() == activeIds.toSet()) { "标签排序数据已变化，请重试" }

        val byId = tags.associateBy { it.id }
        val now = System.currentTimeMillis()
        requestedIds.forEachIndexed { index, tagId ->
            val tag = byId.getValue(tagId)
            if (tag.sortOrder != index) {
                tagDao.update(tag.copy(sortOrder = index, updatedAt = now))
            }
        }
    }

    suspend fun deleteTag(id: Long) {
        tagDao.softDelete(id)
    }
}
