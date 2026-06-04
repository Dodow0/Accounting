package com.dodo.accounting.data.local

import androidx.room.TypeConverter
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.BudgetPeriod
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.SyncState
import com.dodo.accounting.data.local.entity.TransactionSource
import com.dodo.accounting.data.local.entity.TransactionType

class EnumConverters {
    @TypeConverter fun accountTypeToString(value: AccountType): String = value.name
    @TypeConverter fun stringToAccountType(value: String): AccountType = AccountType.valueOf(value)

    @TypeConverter fun categoryKindToString(value: CategoryKind): String = value.name
    @TypeConverter fun stringToCategoryKind(value: String): CategoryKind = CategoryKind.valueOf(value)

    @TypeConverter fun transactionTypeToString(value: TransactionType): String = value.name
    @TypeConverter fun stringToTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter fun transactionSourceToString(value: TransactionSource): String = value.name
    @TypeConverter fun stringToTransactionSource(value: String): TransactionSource = TransactionSource.valueOf(value)

    @TypeConverter fun syncStateToString(value: SyncState): String = value.name
    @TypeConverter fun stringToSyncState(value: String): SyncState = SyncState.valueOf(value)

    @TypeConverter fun budgetPeriodToString(value: BudgetPeriod): String = value.name
    @TypeConverter fun stringToBudgetPeriod(value: String): BudgetPeriod = BudgetPeriod.valueOf(value)
}
