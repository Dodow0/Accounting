package com.dodo.accounting.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = "accounts",
    indices = [
        Index(value = ["uuid"], unique = true),
        Index(value = ["isArchived"]),
        Index(value = ["sortOrder"])
    ]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uuid: String = UUID.randomUUID().toString(),
    val name: String,
    val type: AccountType,
    val currencyCode: String = "CNY",
    val initialBalanceCents: Long = 0,
    val isArchived: Boolean = false,
    val sortOrder: Int = 0,
    val colorArgb: Long = 0xFF2563EB,
    val iconName: String = "account_balance_wallet",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null,
    val syncState: SyncState = SyncState.LOCAL_ONLY
)
