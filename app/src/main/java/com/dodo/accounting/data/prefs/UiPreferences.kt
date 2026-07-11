package com.dodo.accounting.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.dodo.accounting.data.local.entity.TransactionType

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

internal data class EntryPreferences(
    val defaultType: TransactionType = TransactionType.EXPENSE,
    val defaultAccountId: Long? = null,
    val useCurrentTime: Boolean = true,
    val continueAfterSave: Boolean = false,
    val voiceEnabled: Boolean = true,
    val commonCategoryFirst: Boolean = true,
    val tagSuggestionsEnabled: Boolean = true
)

internal data class WebDavConfig(
    val url: String = "",
    val username: String = "",
    val password: String = ""
) {
    val isConfigured: Boolean
        get() = url.trim().isNotBlank()
}

internal data class StoredUiPreferences(
    val amountsHidden: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val entryPreferences: EntryPreferences = EntryPreferences(),
    val webDavConfig: WebDavConfig = WebDavConfig()
)

internal class UiPreferenceStore(context: Context) {
    private val preferences: SharedPreferences = context.applicationContext.getSharedPreferences(
        "accounting_ui_preferences",
        Context.MODE_PRIVATE
    )

    fun load(): StoredUiPreferences {
        return StoredUiPreferences(
            amountsHidden = preferences.getBoolean(KEY_AMOUNTS_HIDDEN, false),
            themeMode = preferences.getEnum(KEY_THEME_MODE, ThemeMode.LIGHT),
            entryPreferences = EntryPreferences(
                defaultType = preferences.getEnum(KEY_ENTRY_DEFAULT_TYPE, TransactionType.EXPENSE).asEntryDefaultType(),
                defaultAccountId = preferences.getLongOrNull(KEY_ENTRY_DEFAULT_ACCOUNT_ID),
                useCurrentTime = preferences.getBoolean(KEY_ENTRY_USE_CURRENT_TIME, true),
                continueAfterSave = preferences.getBoolean(KEY_ENTRY_CONTINUE_AFTER_SAVE, false),
                voiceEnabled = preferences.getBoolean(KEY_ENTRY_VOICE_ENABLED, true),
                commonCategoryFirst = preferences.getBoolean(KEY_ENTRY_COMMON_CATEGORY_FIRST, true),
                tagSuggestionsEnabled = preferences.getBoolean(KEY_ENTRY_TAG_SUGGESTIONS_ENABLED, true)
            ),
            webDavConfig = WebDavConfig(
                url = preferences.getString(KEY_WEBDAV_URL, "").orEmpty(),
                username = preferences.getString(KEY_WEBDAV_USERNAME, "").orEmpty(),
                password = preferences.getString(KEY_WEBDAV_PASSWORD, "").orEmpty()
            )
        )
    }

    fun saveAmountsHidden(hidden: Boolean) {
        preferences.edit().putBoolean(KEY_AMOUNTS_HIDDEN, hidden).apply()
    }

    fun saveThemeMode(mode: ThemeMode) {
        preferences.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }

    fun saveEntryPreferences(value: EntryPreferences) {
        preferences.edit()
            .putString(KEY_ENTRY_DEFAULT_TYPE, value.defaultType.asEntryDefaultType().name)
            .putLongOrRemove(KEY_ENTRY_DEFAULT_ACCOUNT_ID, value.defaultAccountId)
            .putBoolean(KEY_ENTRY_USE_CURRENT_TIME, value.useCurrentTime)
            .putBoolean(KEY_ENTRY_CONTINUE_AFTER_SAVE, value.continueAfterSave)
            .putBoolean(KEY_ENTRY_VOICE_ENABLED, value.voiceEnabled)
            .putBoolean(KEY_ENTRY_COMMON_CATEGORY_FIRST, value.commonCategoryFirst)
            .putBoolean(KEY_ENTRY_TAG_SUGGESTIONS_ENABLED, value.tagSuggestionsEnabled)
            .apply()
    }

    fun saveWebDavConfig(value: WebDavConfig) {
        preferences.edit()
            .putString(KEY_WEBDAV_URL, value.url.trim())
            .putString(KEY_WEBDAV_USERNAME, value.username.trim())
            .putString(KEY_WEBDAV_PASSWORD, value.password)
            .apply()
    }

    private inline fun <reified T : Enum<T>> SharedPreferences.getEnum(key: String, defaultValue: T): T {
        val rawValue = getString(key, null) ?: return defaultValue
        return enumValues<T>().firstOrNull { it.name == rawValue } ?: defaultValue
    }

    private fun SharedPreferences.getLongOrNull(key: String): Long? {
        return if (contains(key)) getLong(key, 0L) else null
    }

    private fun SharedPreferences.Editor.putLongOrRemove(key: String, value: Long?): SharedPreferences.Editor {
        return if (value == null) remove(key) else putLong(key, value)
    }

    private fun TransactionType.asEntryDefaultType(): TransactionType {
        return if (this == TransactionType.BALANCE_ADJUSTMENT) TransactionType.EXPENSE else this
    }

    private companion object {
        const val KEY_AMOUNTS_HIDDEN = "amounts_hidden"
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_ENTRY_DEFAULT_TYPE = "entry_default_type"
        const val KEY_ENTRY_DEFAULT_ACCOUNT_ID = "entry_default_account_id"
        const val KEY_ENTRY_USE_CURRENT_TIME = "entry_use_current_time"
        const val KEY_ENTRY_CONTINUE_AFTER_SAVE = "entry_continue_after_save"
        const val KEY_ENTRY_VOICE_ENABLED = "entry_voice_enabled"
        const val KEY_ENTRY_COMMON_CATEGORY_FIRST = "entry_common_category_first"
        const val KEY_ENTRY_TAG_SUGGESTIONS_ENABLED = "entry_tag_suggestions_enabled"
        const val KEY_WEBDAV_URL = "webdav_url"
        const val KEY_WEBDAV_USERNAME = "webdav_username"
        const val KEY_WEBDAV_PASSWORD = "webdav_password"
    }
}
