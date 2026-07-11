package com.dodo.accounting.ui.screen

import com.dodo.accounting.domain.voice.VoiceParseContext
import com.dodo.accounting.ui.viewmodel.EntryUiState

fun EntryUiState.toVoiceParseContext(): VoiceParseContext = VoiceParseContext(
    activeAccounts = activeAccounts,
    expenseCategories = expenseCategories,
    incomeCategories = incomeCategories,
    tags = tags,
    recentTransactions = recentTransactions
)
