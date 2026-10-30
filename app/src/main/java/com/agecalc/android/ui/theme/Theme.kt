package com.agecalc.android.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private const val THEME_TRANSITION_MS = 240

@Composable
private fun animateThemeColor(target: Color, label: String): Color {
    val animated by animateColorAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = THEME_TRANSITION_MS),
        label = label
    )
    return animated
}

@Composable
fun AgeCalculatorTheme(
    themeOption: AppThemeOption = AppThemeOption.DEFAULT,
    content: @Composable () -> Unit
) {
    val target = themeOption.colors

    val animatedColors = AppThemeColors(
        primary = animateThemeColor(target.primary, "primary"),
        onPrimary = animateThemeColor(target.onPrimary, "onPrimary"),
        secondary = animateThemeColor(target.secondary, "secondary"),
        background = animateThemeColor(target.background, "background"),
        cardSurface = animateThemeColor(target.cardSurface, "cardSurface"),
        fieldBackground = animateThemeColor(target.fieldBackground, "fieldBackground"),
        textPrimary = animateThemeColor(target.textPrimary, "textPrimary"),
        textSecondary = animateThemeColor(target.textSecondary, "textSecondary"),
        subtleBorder = animateThemeColor(target.subtleBorder, "subtleBorder"),
        accentBorder = animateThemeColor(target.accentBorder, "accentBorder"),
        softContainer = animateThemeColor(target.softContainer, "softContainer"),
        buttonGradientStart = animateThemeColor(target.buttonGradientStart, "buttonGradientStart"),
        buttonGradientEnd = animateThemeColor(target.buttonGradientEnd, "buttonGradientEnd"),
        onButton = animateThemeColor(target.onButton, "onButton"),
        isDark = target.isDark
    )

    val materialColorScheme = if (target.isDark) {
        darkColorScheme(
            primary = animatedColors.primary,
            onPrimary = animatedColors.onPrimary,
            primaryContainer = animatedColors.softContainer,
            onPrimaryContainer = animatedColors.textPrimary,
            secondary = animatedColors.secondary,
            onSecondary = animatedColors.onButton,
            background = animatedColors.background,
            onBackground = animatedColors.textPrimary,
            surface = animatedColors.cardSurface,
            onSurface = animatedColors.textPrimary,
            onSurfaceVariant = animatedColors.textSecondary,
            surfaceContainer = animatedColors.cardSurface,
            outline = animatedColors.accentBorder,
            outlineVariant = animatedColors.subtleBorder
        )
    } else {
        lightColorScheme(
            primary = animatedColors.primary,
            onPrimary = animatedColors.onPrimary,
            primaryContainer = animatedColors.softContainer,
            onPrimaryContainer = animatedColors.textPrimary,
            secondary = animatedColors.secondary,
            onSecondary = animatedColors.onButton,
            background = animatedColors.background,
            onBackground = animatedColors.textPrimary,
            surface = animatedColors.cardSurface,
            onSurface = animatedColors.textPrimary,
            onSurfaceVariant = animatedColors.textSecondary,
            surfaceContainer = animatedColors.cardSurface,
            outline = animatedColors.accentBorder,
            outlineVariant = animatedColors.subtleBorder
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            @Suppress("DEPRECATION")
            window.statusBarColor = animatedColors.background.toArgb()
            @Suppress("DEPRECATION")
            window.navigationBarColor = animatedColors.background.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !target.isDark
            controller.isAppearanceLightNavigationBars = !target.isDark
        }
    }

    CompositionLocalProvider(
        LocalAppThemeColors provides animatedColors,
        LocalAppThemeOption provides themeOption
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
