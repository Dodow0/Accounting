package com.dodo.accounting.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val PrimaryBlueGray = Color(0xFF5C7C8A)
val BackgroundGray = Color(0xFFF7F8FA)
val SurfaceWhite = Color(0xFFFFFFFF)
val BorderGray = Color(0xFFE5E7EB)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF374151)
val TextTertiary = Color(0xFF8A8A8E)
val IncomeGreen = Color(0xFF81C784)
val ExpenseRed = Color(0xFFE57373)

private fun lightScheme(): ColorScheme = lightColorScheme(
    primary = Color(0xFF5C7C8A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEEF4F6),
    onPrimaryContainer = Color(0xFF5C7C8A),
    secondary = Color(0xFF81C784),
    onSecondary = Color.White,
    tertiary = Color(0xFFEA580C),
    background = Color(0xFFF7F8FA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF2F4F7),
    onSurfaceVariant = Color(0xFF8A8A8E),
    outline = Color(0xFFD6DAE0),
    outlineVariant = Color(0xFFE5E7EB),
    error = Color(0xFFE57373),
    onError = Color.White
)

private fun darkScheme(): ColorScheme = darkColorScheme(
    primary = Color(0xFF8FAFBD),
    onPrimary = Color(0xFF101820),
    primaryContainer = Color(0xFF24363E),
    onPrimaryContainer = Color(0xFF9BD89F),
    secondary = Color(0xFF9BD89F),
    onSecondary = Color.Black,
    tertiary = Color(0xFFF59E0B),
    background = Color(0xFF111827),
    onBackground = Color(0xFFF9FAFB),
    surface = Color(0xFF1F2937),
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = Color(0xFF273345),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155),
    error = Color(0xFFF19999),
    onError = Color(0xFF2A1111)
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
