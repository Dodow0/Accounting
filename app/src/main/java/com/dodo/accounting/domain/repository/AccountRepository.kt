package com.dodo.accounting.domain.repository

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.domain.model.AccountRemovalResult
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeAccountBalances(): Flow<List<AccountBalanceRow>>
    fun observeAccounts(): Flow<List<AccountEntity>>
    fun observeActiveAccounts(): Flow<List<AccountEntity>>

    suspend fun addAccount(account: AccountEntity): Long
    suspend fun updateAccount(
        id: Long,
        name: String,
        initialBalanceCents: Long,
        iconName: String,
        colorArgb: Long
    )
    suspend fun archiveAccount(id: Long, archived: Boolean): AccountRemovalResult
    suspend fun deleteAccount(id: Long): AccountRemovalResult
    suspend fun reorderAccounts(ids: List<Long>)
}
