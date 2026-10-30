package com.agecalc.android.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agecalc.android.R
import com.agecalc.android.ui.theme.AppThemeOption
import com.agecalc.android.ui.theme.LocalAppThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePickerBottomSheet(
    selectedTheme: AppThemeOption,
    onSelectTheme: (AppThemeOption) -> Unit,
    onDismiss: () -> Unit
) {
    val activeColors = LocalAppThemeColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Neutral sheet surface so every theme button's own tinted background remains clearly distinguishable
    val neutralSheetContainer = if (activeColors.isDark) Color(0xFF12101A) else Color(0xFFF8FAFC)
    val sheetTitleColor = if (activeColors.isDark) Color(0xFFF5F3FF) else Color(0xFF1E293B)
    val sheetSubtitleColor = if (activeColors.isDark) Color(0xFFA39BB8) else Color(0xFF64748B)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = neutralSheetContainer,
        contentColor = sheetTitleColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.theme_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = sheetTitleColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.theme_sheet_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = sheetSubtitleColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 148.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(AppThemeOption.entries) { option ->
                    PaletteSelfStyledThemeButton(
                        option = option,
                        isSelected = option == selectedTheme,
                        onClick = { onSelectTheme(option) }
                    )
                }
            }
        }
    }
}

/**
 * Each theme button renders its OWN primary color, complementary light/dark background,
 * matching border, dual-color preview swatch, and selection checkmark with press animation.
 */
@Composable
private fun PaletteSelfStyledThemeButton(
    option: AppThemeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val palette = option.colors
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.965f else 1f,
        animationSpec = tween(durationMillis = 110),
        label = "theme_button_press_scale"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = palette.background,
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) palette.primary else palette.accentBorder.copy(alpha = 0.75f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Dual-circle preview showing Primary alongside its matching Background color
            Box(
                modifier = Modifier.width(34.dp).height(24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(palette.primary)
                )
                Box(
                    modifier = Modifier
                        .offset(x = 12.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(palette.cardSurface)
                        .border(1.5.dp, palette.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(palette.background)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.displayName,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = palette.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(palette.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = palette.onPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
