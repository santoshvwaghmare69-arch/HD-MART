package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
  primary = KiranaGreenPrimary,
  onPrimary = KiranaSurface,
  primaryContainer = KiranaMint,
  onPrimaryContainer = KiranaGreenDark,
  secondary = KiranaGold,
  onSecondary = KiranaGreenDark,
  secondaryContainer = KiranaMint,
  onSecondaryContainer = KiranaGreenDark,
  tertiary = KiranaOrange,
  onTertiary = KiranaSurface,
  background = KiranaBackground,
  onBackground = KiranaTextPrimary,
  surface = KiranaSurface,
  onSurface = KiranaTextPrimary,
  surfaceVariant = KiranaSurfaceVariant,
  onSurfaceVariant = KiranaTextSecondary,
  outline = KiranaBorder,
)

private val DarkColorScheme = darkColorScheme(
  primary = KiranaGreenLight,
  onPrimary = KiranaGreenDark,
  primaryContainer = KiranaGreenDark,
  onPrimaryContainer = KiranaMint,
  secondary = KiranaGold,
  onSecondary = KiranaGreenDark,
  background = KiranaGreenDark,
  onBackground = KiranaBackground,
  surface = Color(0xFF193B2E),
  onSurface = KiranaBackground,
  surfaceVariant = Color(0xFF224B3B),
  onSurfaceVariant = Color(0xFFB0C4B8),
  outline = Color(0xFF385E4D),
)

@Composable
fun HDMartTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = KiranaGreenPrimary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  HDMartTheme(darkTheme = darkTheme, content = content)
}
