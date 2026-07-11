package com.dodo.accounting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dodo.accounting.data.local.dao.AccountDao
import com.dodo.accounting.data.local.dao.BudgetDao
import com.dodo.accounting.data.local.dao.CategoryDao
import com.dodo.accounting.data.local.dao.RecurringRuleDao
import com.dodo.accounting.data.local.dao.TagDao
import com.dodo.accounting.data.local.dao.TransactionDao
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionTagCrossRef

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TagEntity::class,
        TransactionEntity::class,
        TransactionTagCrossRef::class,
        BudgetEntity::class,
        RecurringRuleEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(EnumConverters::class)
abstract class AccountingDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun tagDao(): TagDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun recurringRuleDao(): RecurringRuleDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tags ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
                db.query("SELECT id FROM tags WHERE deletedAt IS NULL ORDER BY name ASC").use { cursor ->
                    var sortOrder = 0
                    val idIndex = cursor.getColumnIndexOrThrow("id")
                    while (cursor.moveToNext()) {
                        db.execSQL(
                            "UPDATE tags SET sortOrder = ? WHERE id = ?",
                            arrayOf(sortOrder, cursor.getLong(idIndex))
                        )
                        sortOrder += 1
                    }
                }
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS accounts_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        uuid TEXT NOT NULL,
                        name TEXT NOT NULL,
                        currencyCode TEXT NOT NULL,
                        initialBalanceCents INTEGER NOT NULL,
                        isArchived INTEGER NOT NULL,
                        sortOrder INTEGER NOT NULL,
                        colorArgb INTEGER NOT NULL,
                        iconName TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        deletedAt INTEGER,
                        syncState TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO accounts_new (
                        id, uuid, name, currencyCode, initialBalanceCents, isArchived,
                        sortOrder, colorArgb, iconName, createdAt, updatedAt, deletedAt, syncState
                    )
                    SELECT
                        id, uuid, name, currencyCode, initialBalanceCents, isArchived,
                        sortOrder, colorArgb, iconName, createdAt, updatedAt, deletedAt, syncState
                    FROM accounts
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE accounts")
                db.execSQL("ALTER TABLE accounts_new RENAME TO accounts")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_accounts_uuid ON accounts (uuid)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_accounts_isArchived ON accounts (isArchived)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_accounts_sortOrder ON accounts (sortOrder)")
            }
        }
    }
}
