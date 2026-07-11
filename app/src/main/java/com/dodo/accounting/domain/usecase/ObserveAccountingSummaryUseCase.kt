package com.dodo.accounting.domain.usecase

import com.dodo.accounting.domain.model.AccountingSummary
import com.dodo.accounting.domain.model.StatsPeriod
import com.dodo.accounting.domain.model.rangeContaining
import com.dodo.accounting.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class ObserveAccountingSummaryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(period: StatsPeriod, date: LocalDate = LocalDate.now()): Flow<AccountingSummary> {
        val range = period.rangeContaining(date)
        return combine(
            repository.observePeriodSummary(range.startMillis, range.endMillis),
            repository.observeExpenseByCategory(range.startMillis, range.endMillis)
        ) { totals, expenseByCategory ->
            AccountingSummary(
                periodLabel = range.label,
                period = period,
                totals = totals,
                expenseByCategory = expenseByCategory
            )
        }
    }
}
