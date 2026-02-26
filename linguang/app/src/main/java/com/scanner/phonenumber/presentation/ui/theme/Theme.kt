package com.scanner.phonenumber.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFF4CAF50),
    tertiary = Color(0xFFFF9800)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2196F3),
    secondary = Color(0xFF4CAF50),
    tertiary = Color(0xFFFF9800)
)

@Composable
fun PhoneNumberScannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}