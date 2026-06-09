package com.tasnimulhasan.myalarm.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Primary, onPrimary = OnPrimary, primaryContainer = PrimaryVariant,
    secondary = Secondary, onSecondary = OnSecondary,
    background = Background, surface = Surface, surfaceVariant = SurfaceVariant,
    onBackground = TextPrimary, onSurface = TextPrimary, onSurfaceVariant = TextSecondary,
    outline = Outline, error = Error, onError = OnError
)

@Composable
fun MyAlarmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(colorScheme = DarkColorScheme, typography = AppTypography, content = content)
}