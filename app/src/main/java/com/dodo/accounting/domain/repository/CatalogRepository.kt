package com.dodo.accounting.domain.repository

import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.TagEntity
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun observeCategories(): Flow<List<CategoryEntity>>
    fun observeCategories(kind: CategoryKind): Flow<List<CategoryEntity>>
    fun observeTags(): Flow<List<TagEntity>>

    suspend fun addCategory(category: CategoryEntity): Long
    suspend fun renameCategory(id: Long, name: String)
    suspend fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long)
    suspend fun moveCategory(id: Long, direction: Int)
    suspend fun reorderCategories(ids: List<Long>)
    suspend fun deleteCategory(id: Long)

    suspend fun addTag(name: String): Long
    suspend fun renameTag(id: Long, name: String)
    suspend fun reorderTags(ids: List<Long>)
    suspend fun deleteTag(id: Long)
}
