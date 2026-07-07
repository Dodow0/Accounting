package com.dodo.accounting

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.dodo.accounting.ui.screen.AccountingApp
import com.dodo.accounting.ui.screen.ThemeMode
import com.dodo.accounting.ui.screen.UiPreferenceStore
import com.dodo.accounting.ui.theme.AccountingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            val preferenceStore = remember { UiPreferenceStore(this) }
            var themeMode by remember { mutableStateOf(preferenceStore.load().themeMode) }
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> systemDark
            }

            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            AccountingTheme(darkTheme = darkTheme) {
                AccountingApp(
                    themeMode = themeMode,
                    onThemeModeChange = {
                        themeMode = it
                        preferenceStore.saveThemeMode(it)
                    }
                )
            }
        }
    }
}
