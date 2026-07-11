package com.dodo.accounting.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.usecase.EnsureSeedDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Activity-scoped session bootstrap: seed data + due recurring generation.
 * Snackbar text goes through [UiMessenger].
 */
@HiltViewModel
class AppSessionViewModel @Inject constructor(
    private val repository: AccountingRepository,
    private val ensureSeedData: EnsureSeedDataUseCase,
    private val messenger: UiMessenger
) : ViewModel() {

    val messages: SharedFlow<String> = messenger.messages

    init {
        viewModelScope.launch {
            runCatching {
                ensureSeedData()
                repository.generateDueRecurringTransactions()
            }
                .onSuccess { result ->
                    if (result.skippedCount > 0) {
                        messenger.show(
                            "周期账单已自动生成 ${result.generatedCount} 条，已跳过 ${result.skippedCount} 条超出上限的过期账单"
                        )
                    }
                }
                .onFailure { messenger.show(it.message ?: "初始化默认数据失败") }
        }
    }
}
