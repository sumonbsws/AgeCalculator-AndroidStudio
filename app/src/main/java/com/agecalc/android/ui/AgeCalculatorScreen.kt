package com.agecalc.android.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agecalc.android.R
import com.agecalc.android.domain.AgeCalculatorEngine
import com.agecalc.android.domain.AgeResult
import com.agecalc.android.ui.theme.AppThemeOption
import com.agecalc.android.ui.theme.LocalAppThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCalculatorScreen(
    uiState: AgeCalculatorUiState,
    onOpenBirthDatePicker: () -> Unit,
    onOpenTodayDatePicker: () -> Unit,
    onDismissDatePicker: () -> Unit,
    onBirthDateSelected: (Long?) -> Unit,
    onTodayDateSelected: (Long?) -> Unit,
    onCalculateClick: () -> Unit,
    onResetClick: () -> Unit,
    onExitRequest: () -> Unit,
    onDismissExitDialog: () -> Unit,
    onConfirmExit: () -> Unit,
    onOpenThemePicker: () -> Unit,
    onDismissThemePicker: () -> Unit,
    onSelectTheme: (AppThemeOption) -> Unit,
    onOpenAboutDeveloper: () -> Unit,
    onCloseAboutDeveloper: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppThemeColors.current

    val selectedTodayUtcMillis = remember(uiState.todayDate) {
        AgeCalculatorEngine.localDateToUtcMillis(uiState.todayDate)
    }

    if (uiState.isAboutDeveloperVisible) {
        AboutDeveloperSheet(
            onDismiss = onCloseAboutDeveloper
        )
    }

    if (uiState.isThemeSheetVisible) {
        ThemePickerBottomSheet(
            selectedTheme = uiState.selectedTheme,
            onSelectTheme = onSelectTheme,
            onDismiss = onDismissThemePicker
        )
    }

    val datePickerColors = DatePickerDefaults.colors(
        containerColor = theme.cardSurface,
        titleContentColor = theme.textSecondary,
        headlineContentColor = theme.textPrimary,
        weekdayContentColor = theme.textSecondary,
        subheadContentColor = theme.textPrimary,
        navigationContentColor = theme.textPrimary,
        yearContentColor = theme.textPrimary,
        currentYearContentColor = theme.primary,
        selectedYearContentColor = theme.onPrimary,
        selectedYearContainerColor = theme.primary,
        dayContentColor = theme.textPrimary,
        selectedDayContentColor = theme.onPrimary,
        selectedDayContainerColor = theme.primary,
        todayContentColor = theme.primary,
        todayDateBorderColor = theme.primary
    )

    // 1. Date of Birth Calendar Picker (prevents selecting any date later than selected Today's Date)
    if (uiState.activeDatePicker == DatePickerTarget.BIRTH_DATE) {
        val maxYear = maxOf(1900, uiState.todayDate.year)
        val birthPickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedBirthDate?.let {
                AgeCalculatorEngine.localDateToUtcMillis(it)
            },
            initialDisplayedMonthMillis = AgeCalculatorEngine.localDateToUtcMillis(
                uiState.selectedBirthDate ?: uiState.todayDate
            ),
            yearRange = 1900..maxYear,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= selectedTodayUtcMillis
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year <= uiState.todayDate.year
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = onDismissDatePicker,
            colors = DatePickerDefaults.colors(containerColor = theme.cardSurface),
            confirmButton = {
                TextButton(
                    onClick = { onBirthDateSelected(birthPickerState.selectedDateMillis) },
                    enabled = birthPickerState.selectedDateMillis != null,
                    colors = ButtonDefaults.textButtonColors(contentColor = theme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_confirm),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissDatePicker,
                    colors = ButtonDefaults.textButtonColors(contentColor = theme.textSecondary)
                ) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        ) {
            DatePicker(
                state = birthPickerState,
                showModeToggle = false,
                title = {
                    Text(
                        text = stringResource(R.string.picker_title_birth_date),
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                    )
                },
                colors = datePickerColors
            )
        }
    }

    // 2. Today's Date Calendar Picker (supports selecting any past or future reference date)
    if (uiState.activeDatePicker == DatePickerTarget.TODAY_DATE) {
        val todayPickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedTodayUtcMillis,
            initialDisplayedMonthMillis = selectedTodayUtcMillis,
            yearRange = 1900..2100
        )

        DatePickerDialog(
            onDismissRequest = onDismissDatePicker,
            colors = DatePickerDefaults.colors(containerColor = theme.cardSurface),
            confirmButton = {
                TextButton(
                    onClick = { onTodayDateSelected(todayPickerState.selectedDateMillis) },
                    enabled = todayPickerState.selectedDateMillis != null,
                    colors = ButtonDefaults.textButtonColors(contentColor = theme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_confirm),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissDatePicker,
                    colors = ButtonDefaults.textButtonColors(contentColor = theme.textSecondary)
                ) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        ) {
            DatePicker(
                state = todayPickerState,
                showModeToggle = false,
                title = {
                    Text(
                        text = stringResource(R.string.picker_title_today_date),
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                    )
                },
                colors = datePickerColors
            )
        }
    }

    if (uiState.isExitDialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissExitDialog,
            containerColor = theme.cardSurface,
            titleContentColor = theme.textPrimary,
            textContentColor = theme.textSecondary,
            title = {
                Text(
                    text = stringResource(R.string.exit_dialog_title),
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = { Text(stringResource(R.string.exit_dialog_message)) },
            confirmButton = {
                Button(
                    onClick = onConfirmExit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme.buttonGradientStart,
                        contentColor = theme.onButton
                    )
                ) {
                    Text(stringResource(R.string.btn_exit))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissExitDialog,
                    colors = ButtonDefaults.textButtonColors(contentColor = theme.textSecondary)
                ) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = theme.background,
        topBar = {
            PremiumThemedHeader(
                selectedTheme = uiState.selectedTheme,
                onOpenThemePicker = onOpenThemePicker,
                onOpenAboutDeveloper = onOpenAboutDeveloper
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.footer_developed_by),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = theme.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(role = Role.Button, onClick = onOpenAboutDeveloper)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth >= 600.dp
            val scrollState = rememberScrollState()

            if (isWideScreen) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    DateInputCard(
                        uiState = uiState,
                        onOpenBirthDatePicker = onOpenBirthDatePicker,
                        onOpenTodayDatePicker = onOpenTodayDatePicker,
                        onCalculateClick = onCalculateClick,
                        onResetClick = onResetClick,
                        onExitRequest = onExitRequest,
                        modifier = Modifier.weight(1f)
                    )
                    AgeResultCard(
                        calculatedResult = uiState.calculatedResult,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    DateInputCard(
                        uiState = uiState,
                        onOpenBirthDatePicker = onOpenBirthDatePicker,
                        onOpenTodayDatePicker = onOpenTodayDatePicker,
                        onCalculateClick = onCalculateClick,
                        onResetClick = onResetClick,
                        onExitRequest = onExitRequest,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AgeResultCard(
                        calculatedResult = uiState.calculatedResult,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumThemedHeader(
    selectedTheme: AppThemeOption,
    onOpenThemePicker: () -> Unit,
    onOpenAboutDeveloper: () -> Unit
) {
    val theme = LocalAppThemeColors.current

    Surface(
        color = theme.background,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = theme.softContainer,
                    border = BorderStroke(1.dp, theme.accentBorder.copy(alpha = 0.55f)),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = theme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = theme.textPrimary
                    )
                    Text(
                        text = stringResource(R.string.subtitle_version),
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.textSecondary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // About Developer Info Button
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = theme.cardSurface,
                    border = BorderStroke(1.dp, theme.subtleBorder),
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(
                            role = Role.Button,
                            onClick = onOpenAboutDeveloper
                        )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.cd_open_about),
                            tint = theme.primary,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Palette Button opening the 10-Theme Customization Bottom Sheet
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = theme.cardSurface,
                    border = BorderStroke(1.dp, theme.subtleBorder),
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(
                            role = Role.Button,
                            onClick = onOpenThemePicker
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 11.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = rememberPaletteIcon(),
                            contentDescription = stringResource(R.string.cd_open_theme_picker),
                            tint = theme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(selectedTheme.colors.primary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateInputCard(
    uiState: AgeCalculatorUiState,
    onOpenBirthDatePicker: () -> Unit,
    onOpenTodayDatePicker: () -> Unit,
    onCalculateClick: () -> Unit,
    onResetClick: () -> Unit,
    onExitRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppThemeColors.current

    val dobInteractionSource = remember { MutableInteractionSource() }
    LaunchedEffect(dobInteractionSource) {
        dobInteractionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                onOpenBirthDatePicker()
            }
        }
    }

    val todayInteractionSource = remember { MutableInteractionSource() }
    LaunchedEffect(todayInteractionSource) {
        todayInteractionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                onOpenTodayDatePicker()
            }
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
        border = BorderStroke(1.dp, theme.subtleBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = if (theme.isDark) 0.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Date of Birth Selectable Calendar Field
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.label_date_of_birth),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.textPrimary
                )

                OutlinedTextField(
                    value = uiState.selectedBirthDate?.let { AgeCalculatorEngine.formatDisplayDate(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    interactionSource = dobInteractionSource,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.placeholder_click_here),
                            color = theme.textSecondary
                        )
                    },
                    isError = uiState.errorMessageRes != null,
                    leadingIcon = {
                        IconButton(onClick = onOpenBirthDatePicker) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.cd_open_calendar),
                                tint = theme.primary
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = theme.accentBorder,
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary,
                        focusedContainerColor = theme.fieldBackground,
                        unfocusedContainerColor = theme.fieldBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 2. Today's Date Selectable Calendar Field (defaults to system date; allows past/future selection)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.label_today_date),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.textPrimary
                )

                OutlinedTextField(
                    value = AgeCalculatorEngine.formatDisplayDate(uiState.todayDate),
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    interactionSource = todayInteractionSource,
                    leadingIcon = {
                        IconButton(onClick = onOpenTodayDatePicker) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.cd_open_today_calendar),
                                tint = theme.primary
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = theme.subtleBorder,
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary,
                        focusedContainerColor = theme.fieldBackground,
                        unfocusedContainerColor = theme.fieldBackground
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (uiState.errorMessageRes != null) {
                Text(
                    text = stringResource(uiState.errorMessageRes),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Full-width Theme Gradient Calculate Age Button with pressed-state animation
            ThemedGradientCalculateButton(
                text = stringResource(R.string.btn_calculate_age),
                onClick = onCalculateClick,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = theme.subtleBorder)

            // Outlined Theme Reset and Exit Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onResetClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, theme.accentBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = theme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = theme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.btn_reset),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onExitRequest,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, theme.subtleBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = theme.textSecondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = theme.textSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.btn_exit),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemedGradientCalculateButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppThemeColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.975f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "calculate_button_press_scale"
    )

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(theme.buttonGradientStart, theme.buttonGradientEnd)
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = gradientBrush)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = rememberCalculatorIcon(),
                contentDescription = null,
                tint = theme.onButton,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = theme.onButton
            )
        }
    }
}

@Composable
private fun AgeResultCard(
    calculatedResult: AgeResult?,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppThemeColors.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
        border = BorderStroke(1.dp, theme.subtleBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = if (theme.isDark) 0.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = theme.softContainer,
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = rememberCelebrationCakeIcon(),
                            contentDescription = null,
                            tint = theme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.label_calculated_age),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = theme.textPrimary
                )
            }

            // Three-section Years | Months | Days display
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = theme.fieldBackground,
                border = BorderStroke(1.dp, theme.subtleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 18.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AgeStatSection(
                        value = calculatedResult?.years?.toString() ?: "—",
                        label = stringResource(R.string.unit_years),
                        isCalculated = calculatedResult != null,
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider(
                        color = theme.accentBorder.copy(alpha = 0.55f),
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 4.dp)
                    )
                    AgeStatSection(
                        value = calculatedResult?.months?.toString() ?: "—",
                        label = stringResource(R.string.unit_months),
                        isCalculated = calculatedResult != null,
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider(
                        color = theme.accentBorder.copy(alpha = 0.55f),
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 4.dp)
                    )
                    AgeStatSection(
                        value = calculatedResult?.days?.toString() ?: "—",
                        label = stringResource(R.string.unit_days),
                        isCalculated = calculatedResult != null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Exact formatted result string ("45 Years, 10 Months, 28 Days")
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (calculatedResult != null) theme.softContainer.copy(alpha = 0.65f) else theme.fieldBackground,
                border = BorderStroke(1.dp, theme.subtleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = calculatedResult?.formatted ?: stringResource(R.string.placeholder_result),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (calculatedResult != null) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (calculatedResult != null) theme.textPrimary else theme.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun AgeStatSection(
    value: String,
    label: String,
    isCalculated: Boolean,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppThemeColors.current

    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCalculated) theme.primary else theme.accentBorder,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            fontSize = 11.sp,
            letterSpacing = 0.8.sp,
            fontWeight = FontWeight.SemiBold,
            color = theme.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun rememberPaletteIcon(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "PaletteIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 3f)
                curveTo(7.03f, 3f, 3f, 7.03f, 3f, 12f)
                curveTo(3f, 16.97f, 7.03f, 21f, 12f, 21f)
                curveTo(12.83f, 21f, 13.5f, 20.33f, 13.5f, 19.5f)
                curveTo(13.5f, 19.11f, 13.35f, 18.76f, 13.11f, 18.49f)
                curveTo(12.88f, 18.23f, 12.73f, 17.88f, 12.73f, 17.5f)
                curveTo(12.73f, 16.67f, 13.4f, 16f, 14.23f, 16f)
                horizontalLineTo(16f)
                curveTo(18.76f, 16f, 21f, 13.76f, 21f, 11f)
                curveTo(21f, 6.58f, 16.97f, 3f, 12f, 3f)
                close()
                moveTo(6.5f, 12f)
                curveTo(5.67f, 12f, 5f, 11.33f, 5f, 10.5f)
                curveTo(5f, 9.67f, 5.67f, 9f, 6.5f, 9f)
                curveTo(7.33f, 9f, 8f, 9.67f, 8f, 10.5f)
                curveTo(8f, 11.33f, 7.33f, 12f, 6.5f, 12f)
                close()
                moveTo(9.5f, 8f)
                curveTo(8.67f, 8f, 8f, 7.33f, 8f, 6.5f)
                curveTo(8f, 5.67f, 8.67f, 5f, 9.5f, 5f)
                curveTo(10.33f, 5f, 11f, 5.67f, 11f, 6.5f)
                curveTo(11f, 7.33f, 10.33f, 8f, 9.5f, 8f)
                close()
                moveTo(14.5f, 8f)
                curveTo(13.67f, 8f, 13f, 7.33f, 13f, 6.5f)
                curveTo(13f, 5.67f, 13.67f, 5f, 14.5f, 5f)
                curveTo(15.33f, 5f, 16f, 5.67f, 16f, 6.5f)
                curveTo(16f, 7.33f, 15.33f, 8f, 14.5f, 8f)
                close()
                moveTo(17.5f, 12f)
                curveTo(16.67f, 12f, 16f, 11.33f, 16f, 10.5f)
                curveTo(16f, 9.67f, 16.67f, 9f, 17.5f, 9f)
                curveTo(18.33f, 9f, 19f, 9.67f, 19f, 10.5f)
                curveTo(19f, 11.33f, 18.33f, 12f, 17.5f, 12f)
                close()
            }
        }.build()
    }
}

@Composable
private fun rememberCalculatorIcon(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "CalculatorIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(19f, 3f)
                horizontalLineTo(5f)
                curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
                verticalLineTo(5f)
                curveTo(21f, 3.9f, 20.1f, 3f, 19f, 3f)
                close()
                moveTo(19f, 19f)
                horizontalLineTo(5f)
                verticalLineTo(5f)
                horizontalLineTo(19f)
                verticalLineTo(19f)
                close()
                moveTo(7f, 7f)
                horizontalLineTo(17f)
                verticalLineTo(10f)
                horizontalLineTo(7f)
                verticalLineTo(7f)
                close()
                moveTo(7f, 12f)
                horizontalLineTo(9.5f)
                verticalLineTo(14f)
                horizontalLineTo(7f)
                verticalLineTo(12f)
                close()
                moveTo(10.75f, 12f)
                horizontalLineTo(13.25f)
                verticalLineTo(14f)
                horizontalLineTo(10.75f)
                verticalLineTo(12f)
                close()
                moveTo(14.5f, 12f)
                horizontalLineTo(17f)
                verticalLineTo(17f)
                horizontalLineTo(14.5f)
                verticalLineTo(12f)
                close()
                moveTo(7f, 15.5f)
                horizontalLineTo(9.5f)
                verticalLineTo(17.5f)
                horizontalLineTo(7f)
                verticalLineTo(15.5f)
                close()
                moveTo(10.75f, 15.5f)
                horizontalLineTo(13.25f)
                verticalLineTo(17.5f)
                horizontalLineTo(10.75f)
                verticalLineTo(15.5f)
                close()
            }
        }.build()
    }
}

@Composable
private fun rememberCelebrationCakeIcon(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "CelebrationCakeIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(12f, 6f)
                curveTo(13.11f, 6f, 14f, 5.1f, 14f, 4f)
                curveTo(14f, 3.62f, 13.9f, 3.27f, 13.71f, 2.97f)
                lineTo(12f, 0f)
                lineTo(10.29f, 2.97f)
                curveTo(10.1f, 3.27f, 10f, 3.62f, 10f, 4f)
                curveTo(10f, 5.1f, 10.89f, 6f, 12f, 6f)
                close()
                moveTo(18f, 9f)
                horizontalLineTo(13f)
                verticalLineTo(7f)
                horizontalLineTo(11f)
                verticalLineTo(9f)
                horizontalLineTo(6f)
                curveTo(4.34f, 9f, 3f, 10.34f, 3f, 12f)
                verticalLineTo(21f)
                curveTo(3f, 21.55f, 3.45f, 22f, 4f, 22f)
                horizontalLineTo(20f)
                curveTo(20.55f, 22f, 21f, 21.55f, 21f, 21f)
                verticalLineTo(12f)
                curveTo(21f, 10.34f, 19.66f, 9f, 18f, 9f)
                close()
                moveTo(19f, 20f)
                horizontalLineTo(5f)
                verticalLineTo(16f)
                horizontalLineTo(19f)
                verticalLineTo(20f)
                close()
                moveTo(19f, 14f)
                horizontalLineTo(5f)
                verticalLineTo(12f)
                curveTo(5f, 11.45f, 5.45f, 11f, 6f, 11f)
                horizontalLineTo(18f)
                curveTo(18.55f, 11f, 19f, 11.45f, 19f, 12f)
                verticalLineTo(14f)
                close()
            }
        }.build()
    }
}
