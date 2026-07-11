package com.dodo.accounting.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AssetsUiState(
    val accounts: List<AccountBalanceRow> = emptyList(),
    val isLoading: Boolean = true
) {
    val totalAssetsCents: Long = accounts
        .filterNot { it.account.isArchived }
        .sumOf { it.balanceCents }
}

@HiltViewModel
class AssetsViewModel @Inject constructor(
    accounts: AccountRepository
) : ViewModel() {
    val uiState: StateFlow<AssetsUiState> = accounts.observeAccountBalances()
        .map { accounts ->
            AssetsUiState(accounts = accounts, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AssetsUiState()
        )
}
