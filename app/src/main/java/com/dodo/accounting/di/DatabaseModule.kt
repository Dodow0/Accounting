package com.dodo.accounting.di

import android.content.Context
import androidx.room.Room
import com.dodo.accounting.data.backup.BackupRepositoryImpl
import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.repository.AccountingRepositoryImpl
import com.dodo.accounting.domain.repository.AccountRepository
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.repository.BackupRepository
import com.dodo.accounting.domain.repository.CatalogRepository
import com.dodo.accounting.domain.repository.PlanningRepository
import com.dodo.accounting.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AccountingDatabase {
        return Room.databaseBuilder(
            context,
            AccountingDatabase::class.java,
            "accounting.db"
        )
            .addMigrations(AccountingDatabase.MIGRATION_1_2)
            .addMigrations(AccountingDatabase.MIGRATION_2_3)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAccountingRepository(
        impl: AccountingRepositoryImpl
    ): AccountingRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountingRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        impl: AccountingRepositoryImpl
    ): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: AccountingRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindPlanningRepository(
        impl: AccountingRepositoryImpl
    ): PlanningRepository

    @Binds
    @Singleton
    abstract fun bindBackupRepository(
        impl: BackupRepositoryImpl
    ): BackupRepository
}
