package com.dodo.accounting.ui.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App-wide snackbar messages. Survives ViewModel boundaries so shell can host one SnackbarHost.
 */
@Singleton
class UiMessenger @Inject constructor() {
    private val _messages = MutableSharedFlow<String>(
        extraBufferCapacity = 8,
        replay = 0
    )
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    fun show(message: String) {
        _messages.tryEmit(message)
    }
}
