package com.agecalc.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agecalc.android.data.ThemePreferencesRepository
import com.agecalc.android.ui.AgeCalculatorScreen
import com.agecalc.android.ui.AgeCalculatorViewModel
import com.agecalc.android.ui.theme.AgeCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = remember { ThemePreferencesRepository(applicationContext) }
            val viewModel: AgeCalculatorViewModel = viewModel(
                factory = AgeCalculatorViewModel.provideFactory(repository)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AgeCalculatorTheme(themeOption = uiState.selectedTheme) {
                AgeCalculatorScreen(
                    uiState = uiState,
                    onOpenBirthDatePicker = viewModel::openBirthDatePicker,
                    onOpenTodayDatePicker = viewModel::openTodayDatePicker,
                    onDismissDatePicker = viewModel::dismissDatePicker,
                    onBirthDateSelected = viewModel::onBirthDateSelected,
                    onTodayDateSelected = viewModel::onTodayDateSelected,
                    onCalculateClick = viewModel::calculateAge,
                    onResetClick = viewModel::reset,
                    onExitRequest = viewModel::requestExit,
                    onDismissExitDialog = viewModel::dismissExitDialog,
                    onConfirmExit = {
                        viewModel.dismissExitDialog()
                        finishAffinity()
                    },
                    onOpenThemePicker = viewModel::openThemePicker,
                    onDismissThemePicker = viewModel::dismissThemePicker,
                    onSelectTheme = viewModel::selectTheme,
                    onOpenAboutDeveloper = viewModel::openAboutDeveloper,
                    onCloseAboutDeveloper = viewModel::closeAboutDeveloper
                )
            }
        }
    }
}
