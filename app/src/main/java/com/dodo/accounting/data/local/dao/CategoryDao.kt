package com.dodo.accounting.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE deletedAt IS NULL ORDER BY kind ASC, sortOrder ASC, createdAt ASC")
    fun observeCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories ORDER BY kind ASC, sortOrder ASC, createdAt ASC")
    suspend fun getCategoriesSnapshot(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE deletedAt IS NULL AND kind = :kind ORDER BY sortOrder ASC, createdAt ASC")
    fun observeCategories(kind: CategoryKind): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE deletedAt IS NULL AND kind = :kind ORDER BY sortOrder ASC, createdAt ASC")
    suspend fun getActiveCategories(kind: CategoryKind): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories WHERE deletedAt IS NULL AND kind = :kind AND name = :name LIMIT 1")
    suspend fun getActiveCategoryByName(name: String, kind: CategoryKind): CategoryEntity?

    @Query("SELECT COUNT(*) FROM categories WHERE deletedAt IS NULL")
    suspend fun countActiveCategories(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("UPDATE categories SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM categories")
    suspend fun clearAll()
}
