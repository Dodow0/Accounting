package com.dodo.accounting.data.local.entity

enum class SyncState {
    LOCAL_ONLY,
    PENDING_SYNC,
    SYNCED,
    CONFLICT
}
