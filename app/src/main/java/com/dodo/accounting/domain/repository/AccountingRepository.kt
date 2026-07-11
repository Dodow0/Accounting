package com.dodo.accounting.domain.repository

/**
 * Compatibility facade covering all aggregate repositories.
 * Prefer injecting the smaller interfaces in new code.
 */
interface AccountingRepository :
    AccountRepository,
    CatalogRepository,
    TransactionRepository,
    PlanningRepository,
    BackupRepository
