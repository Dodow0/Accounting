package com.dodo.accounting.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.dodo.accounting.R

@Composable
private fun lightScheme(): ColorScheme = lightColorScheme(
    primary = colorResource(R.color.brand_primary),
    onPrimary = colorResource(android.R.color.white),
    primaryContainer = colorResource(R.color.brand_primary_light),
    onPrimaryContainer = colorResource(R.color.brand_primary),
    secondary = colorResource(R.color.income_amount),
    onSecondary = colorResource(android.R.color.white),
    tertiary = colorResource(R.color.warning_amount),
    background = colorResource(R.color.app_background),
    onBackground = colorResource(R.color.app_on_surface),
    surface = colorResource(R.color.app_surface),
    onSurface = colorResource(R.color.app_on_surface),
    surfaceVariant = colorResource(R.color.app_surface_variant),
    onSurfaceVariant = colorResource(R.color.app_on_surface_variant),
    outline = colorResource(R.color.app_outline),
    outlineVariant = colorResource(R.color.app_outline_variant),
    error = colorResource(R.color.over_budget)
)

@Composable
private fun darkScheme(): ColorScheme = darkColorScheme(
    primary = colorResource(R.color.brand_primary),
    onPrimary = colorResource(android.R.color.white),
    primaryContainer = colorResource(R.color.brand_primary_light),
    onPrimaryContainer = colorResource(R.color.income_amount),
    secondary = colorResource(R.color.income_amount),
    onSecondary = colorResource(android.R.color.black),
    tertiary = colorResource(R.color.warning_amount),
    background = colorResource(R.color.app_background),
    onBackground = colorResource(R.color.app_on_surface),
    surface = colorResource(R.color.app_surface),
    onSurface = colorResource(R.color.app_on_surface),
    surfaceVariant = colorResource(R.color.app_surface_variant),
    onSurfaceVariant = colorResource(R.color.app_on_surface_variant),
    outline = colorResource(R.color.app_outline),
    outlineVariant = colorResource(R.color.app_outline_variant),
    error = colorResource(R.color.over_budget)
)

@Composable
fun AccountingTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme() else lightScheme(),
        typography = AccountingTypography,
        content = content
    )
}
