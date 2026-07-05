package com.dodo.accounting.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.dodo.accounting.R

val PrimaryBlueGray = Color(0xFF5C7C8A)
val BackgroundGray = Color(0xFFF7F8FA)
val SurfaceWhite = Color(0xFFFFFFFF)
val BorderGray = Color(0xFFE5E7EB)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF374151)
val TextTertiary = Color(0xFF8A8A8E)
val IncomeGreen = Color(0xFF81C784)
val ExpenseRed = Color(0xFFE57373)

@Composable
private fun lightScheme(): ColorScheme = lightColorScheme(
    primary = colorResource(R.color.brand_primary),
    onPrimary = Color.White,
    primaryContainer = colorResource(R.color.brand_primary_light),
    onPrimaryContainer = colorResource(R.color.brand_primary),
    secondary = colorResource(R.color.income_amount),
    onSecondary = Color.White,
    tertiary = colorResource(R.color.warning_amount),
    background = colorResource(R.color.app_background),
    onBackground = colorResource(R.color.app_on_surface),
    surface = colorResource(R.color.app_surface),
    onSurface = colorResource(R.color.app_on_surface),
    surfaceVariant = colorResource(R.color.app_surface_variant),
    onSurfaceVariant = colorResource(R.color.app_on_surface_variant),
    outline = colorResource(R.color.app_outline),
    outlineVariant = colorResource(R.color.app_outline_variant),
    error = colorResource(R.color.over_budget),
    onError = Color.White
)

@Composable
private fun darkScheme(): ColorScheme = darkColorScheme(
    primary = colorResource(R.color.brand_primary),
    onPrimary = Color.White,
    primaryContainer = colorResource(R.color.brand_primary_light),
    onPrimaryContainer = colorResource(R.color.income_amount),
    secondary = colorResource(R.color.income_amount),
    onSecondary = Color.Black,
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

val AccountingShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun AccountingTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme() else lightScheme(),
        typography = AccountingTypography,
        shapes = AccountingShapes,
        content = content
    )
}
