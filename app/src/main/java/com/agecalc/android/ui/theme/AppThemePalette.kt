package com.agecalc.android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val background: Color,
    val cardSurface: Color,
    val fieldBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val subtleBorder: Color,
    val accentBorder: Color,
    val softContainer: Color,
    val buttonGradientStart: Color,
    val buttonGradientEnd: Color,
    val onButton: Color,
    val isDark: Boolean
)

enum class AppThemeOption(
    val id: String,
    val displayName: String,
    val colors: AppThemeColors
) {
    VIOLET_DREAM(
        id = "violet_dream",
        displayName = "Violet Dream",
        colors = AppThemeColors(
            primary = Color(0xFF6C3CE9),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFF8B5CF6),
            background = Color(0xFFF7F5FF),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFF7F5FF),
            textPrimary = Color(0xFF241445),
            textSecondary = Color(0xFF79718C),
            subtleBorder = Color(0xFFE3DCFC),
            accentBorder = Color(0xFFC4B5FD),
            softContainer = Color(0xFFEDE7FE),
            buttonGradientStart = Color(0xFF6C3CE9),
            buttonGradientEnd = Color(0xFF8B5CF6),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    OCEAN_BLUE(
        id = "ocean_blue",
        displayName = "Ocean Blue",
        colors = AppThemeColors(
            primary = Color(0xFF1976D2),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFF2563EB),
            background = Color(0xFFF1F7FF),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFF1F7FF),
            textPrimary = Color(0xFF0F294A),
            textSecondary = Color(0xFF5B708B),
            subtleBorder = Color(0xFFD6E6FC),
            accentBorder = Color(0xFF93C5FD),
            softContainer = Color(0xFFE1EFFE),
            buttonGradientStart = Color(0xFF1976D2),
            buttonGradientEnd = Color(0xFF2563EB),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    EMERALD_GREEN(
        id = "emerald_green",
        displayName = "Emerald Green",
        colors = AppThemeColors(
            primary = Color(0xFF16865C),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFF0D9F6E),
            background = Color(0xFFF0FAF5),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFF0FAF5),
            textPrimary = Color(0xFF0D3324),
            textSecondary = Color(0xFF567568),
            subtleBorder = Color(0xFFD1EFE0),
            accentBorder = Color(0xFF86EFAC),
            softContainer = Color(0xFFDCFCE7),
            buttonGradientStart = Color(0xFF16865C),
            buttonGradientEnd = Color(0xFF0D9F6E),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    ROSE_PINK(
        id = "rose_pink",
        displayName = "Rose Pink",
        colors = AppThemeColors(
            primary = Color(0xFFDB2777),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFFEC4899),
            background = Color(0xFFFFF1F7),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFFFF1F7),
            textPrimary = Color(0xFF421027),
            textSecondary = Color(0xFF855D70),
            subtleBorder = Color(0xFFFBD5E5),
            accentBorder = Color(0xFFF9A8D4),
            softContainer = Color(0xFFFCE7F3),
            buttonGradientStart = Color(0xFFDB2777),
            buttonGradientEnd = Color(0xFFEC4899),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    SUNSET_ORANGE(
        id = "sunset_orange",
        displayName = "Sunset Orange",
        colors = AppThemeColors(
            primary = Color(0xFFEA580C),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFFF97316),
            background = Color(0xFFFFF7ED),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFFFF7ED),
            textPrimary = Color(0xFF431A06),
            textSecondary = Color(0xFF8A6552),
            subtleBorder = Color(0xFFFED7AA),
            accentBorder = Color(0xFFFDBA74),
            softContainer = Color(0xFFFFEDD5),
            buttonGradientStart = Color(0xFFEA580C),
            buttonGradientEnd = Color(0xFFF97316),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    MIDNIGHT_DARK(
        id = "midnight_dark",
        displayName = "Midnight Dark",
        colors = AppThemeColors(
            primary = Color(0xFFA78BFA),
            onPrimary = Color(0xFF15131D),
            secondary = Color(0xFF8B5CF6),
            background = Color(0xFF15131D),
            cardSurface = Color(0xFF1F1C2B),
            fieldBackground = Color(0xFF282438),
            textPrimary = Color(0xFFF5F3FF),
            textSecondary = Color(0xFFA39BB8),
            subtleBorder = Color(0xFF332E47),
            accentBorder = Color(0xFF6D5BAE),
            softContainer = Color(0xFF2D2644),
            buttonGradientStart = Color(0xFF7C3AED),
            buttonGradientEnd = Color(0xFF6D28D9),
            onButton = Color(0xFFFFFFFF),
            isDark = true
        )
    ),
    TEAL_FRESH(
        id = "teal_fresh",
        displayName = "Teal Fresh",
        colors = AppThemeColors(
            primary = Color(0xFF0F9690),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFF14B8A6),
            background = Color(0xFFEFFBFA),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFEFFBFA),
            textPrimary = Color(0xFF0B3533),
            textSecondary = Color(0xFF557876),
            subtleBorder = Color(0xFFCCF0EE),
            accentBorder = Color(0xFF5EEAD4),
            softContainer = Color(0xFFCCFBF1),
            buttonGradientStart = Color(0xFF0F9690),
            buttonGradientEnd = Color(0xFF0D9488),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    ROYAL_INDIGO(
        id = "royal_indigo",
        displayName = "Royal Indigo",
        colors = AppThemeColors(
            primary = Color(0xFF4338CA),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFF6366F1),
            background = Color(0xFFF3F2FF),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFF3F2FF),
            textPrimary = Color(0xFF1B1747),
            textSecondary = Color(0xFF66628A),
            subtleBorder = Color(0xFFDCD9FE),
            accentBorder = Color(0xFFA5B4FC),
            softContainer = Color(0xFFE0E7FF),
            buttonGradientStart = Color(0xFF4338CA),
            buttonGradientEnd = Color(0xFF6366F1),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    RUBY_RED(
        id = "ruby_red",
        displayName = "Ruby Red",
        colors = AppThemeColors(
            primary = Color(0xFFDC2626),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFFEF4444),
            background = Color(0xFFFFF4F4),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFFFF4F4),
            textPrimary = Color(0xFF451010),
            textSecondary = Color(0xFF875B5B),
            subtleBorder = Color(0xFFFECACA),
            accentBorder = Color(0xFFFCA5A5),
            softContainer = Color(0xFFFEE2E2),
            buttonGradientStart = Color(0xFFDC2626),
            buttonGradientEnd = Color(0xFFEF4444),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    ),
    GOLDEN_AMBER(
        id = "golden_amber",
        displayName = "Golden Amber",
        colors = AppThemeColors(
            primary = Color(0xFFB7791F),
            onPrimary = Color(0xFFFFFFFF),
            secondary = Color(0xFFD97706),
            background = Color(0xFFFFFBEB),
            cardSurface = Color(0xFFFFFFFF),
            fieldBackground = Color(0xFFFFFBEB),
            textPrimary = Color(0xFF3D2607),
            textSecondary = Color(0xFF826B4B),
            subtleBorder = Color(0xFFFDE68A),
            accentBorder = Color(0xFFFCD34D),
            softContainer = Color(0xFFFEF3C7),
            buttonGradientStart = Color(0xFFB7791F),
            buttonGradientEnd = Color(0xFFD97706),
            onButton = Color(0xFFFFFFFF),
            isDark = false
        )
    );

    companion object {
        val DEFAULT = VIOLET_DREAM

        fun fromId(id: String?): AppThemeOption {
            if (id.isNullOrBlank()) return DEFAULT
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}

val LocalAppThemeColors = staticCompositionLocalOf { AppThemeOption.DEFAULT.colors }
val LocalAppThemeOption = staticCompositionLocalOf { AppThemeOption.DEFAULT }
