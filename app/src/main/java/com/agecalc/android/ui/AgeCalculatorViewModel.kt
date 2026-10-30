package com.agecalc.android.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.agecalc.android.R
import com.agecalc.android.data.ThemePreferencesRepository
import com.agecalc.android.domain.AgeCalculationOutcome
import com.agecalc.android.domain.AgeCalculatorEngine
import com.agecalc.android.domain.AgeResult
import com.agecalc.android.ui.theme.AppThemeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class DatePickerTarget {
    BIRTH_DATE,
    TODAY_DATE
}

data class AgeCalculatorUiState(
    val selectedBirthDate: LocalDate? = null,
    val todayDate: LocalDate = LocalDate.now(),
    val calculatedResult: AgeResult? = null,
    @StringRes val errorMessageRes: Int? = null,
    val activeDatePicker: DatePickerTarget? = null,
    val isExitDialogVisible: Boolean = false,
    val selectedTheme: AppThemeOption = AppThemeOption.DEFAULT,
    val isThemeSheetVisible: Boolean = false,
    val isAboutDeveloperVisible: Boolean = false
)

class AgeCalculatorViewModel(
    private val themeRepository: ThemePreferencesRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgeCalculatorUiState())
    val uiState: StateFlow<AgeCalculatorUiState> = _uiState.asStateFlow()

    init {
        if (themeRepository != null) {
            viewModelScope.launch {
                themeRepository.selectedThemeFlow.collect { savedTheme ->
                    _uiState.update { it.copy(selectedTheme = savedTheme) }
                }
            }
        }
    }

    fun openBirthDatePicker() {
        _uiState.update { it.copy(activeDatePicker = DatePickerTarget.BIRTH_DATE) }
    }

    fun openTodayDatePicker() {
        _uiState.update { it.copy(activeDatePicker = DatePickerTarget.TODAY_DATE) }
    }

    fun dismissDatePicker() {
        _uiState.update { it.copy(activeDatePicker = null) }
    }

    fun onBirthDateSelected(utcTimeMillis: Long?) {
        if (utcTimeMillis == null) {
            dismissDatePicker()
            return
        }

        val chosenBirthDate = AgeCalculatorEngine.utcMillisToLocalDate(utcTimeMillis)
        val referenceToday = _uiState.value.todayDate

        if (chosenBirthDate.isAfter(referenceToday)) {
            _uiState.update {
                it.copy(
                    calculatedResult = null,
                    errorMessageRes = R.string.error_future_birth_date,
                    activeDatePicker = null
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                selectedBirthDate = chosenBirthDate,
                calculatedResult = null,
                errorMessageRes = null,
                activeDatePicker = null
            )
        }
    }

    /**
     * Updates the user-selected "Today's Date" reference without overwriting it with system time.
     * Supports both past and future reference dates.
     */
    fun onTodayDateSelected(utcTimeMillis: Long?) {
        if (utcTimeMillis == null) {
            dismissDatePicker()
            return
        }

        val chosenToday = AgeCalculatorEngine.utcMillisToLocalDate(utcTimeMillis)
        val currentBirthDate = _uiState.value.selectedBirthDate
        val isInvalidOrder = currentBirthDate != null && currentBirthDate.isAfter(chosenToday)

        _uiState.update {
            it.copy(
                todayDate = chosenToday,
                calculatedResult = null,
                errorMessageRes = if (isInvalidOrder) R.string.error_future_birth_date else null,
                activeDatePicker = null
            )
        }
    }

    /**
     * Calculates age using the selected Date of Birth and the selected Today's Date.
     */
    fun calculateAge() {
        val currentState = _uiState.value
        val birthDate = currentState.selectedBirthDate
        val referenceToday = currentState.todayDate

        when (val outcome = AgeCalculatorEngine.calculateAge(birthDate, referenceToday)) {
            is AgeCalculationOutcome.Success -> {
                _uiState.update {
                    it.copy(
                        calculatedResult = outcome.result,
                        errorMessageRes = null
                    )
                }
            }
            AgeCalculationOutcome.MissingBirthDate -> {
                _uiState.update {
                    it.copy(
                        calculatedResult = null,
                        errorMessageRes = R.string.error_missing_birth_date
                    )
                }
            }
            AgeCalculationOutcome.FutureBirthDate -> {
                _uiState.update {
                    it.copy(
                        calculatedResult = null,
                        errorMessageRes = R.string.error_future_birth_date
                    )
                }
            }
        }
    }

    /**
     * Resets Date of Birth to empty, Today's Date to current system date, and clears the result
     * while preserving the active color theme.
     */
    fun reset() {
        _uiState.update { current ->
            AgeCalculatorUiState(
                selectedBirthDate = null,
                todayDate = LocalDate.now(),
                calculatedResult = null,
                errorMessageRes = null,
                selectedTheme = current.selectedTheme
            )
        }
    }

    fun requestExit() {
        _uiState.update { it.copy(isExitDialogVisible = true) }
    }

    fun dismissExitDialog() {
        _uiState.update { it.copy(isExitDialogVisible = false) }
    }

    fun openThemePicker() {
        _uiState.update { it.copy(isThemeSheetVisible = true) }
    }

    fun dismissThemePicker() {
        _uiState.update { it.copy(isThemeSheetVisible = false) }
    }

    fun selectTheme(option: AppThemeOption) {
        _uiState.update { it.copy(selectedTheme = option) }
        if (themeRepository != null) {
            viewModelScope.launch {
                themeRepository.saveTheme(option)
            }
        }
    }

    fun openAboutDeveloper() {
        _uiState.update { it.copy(isAboutDeveloperVisible = true) }
    }

    fun closeAboutDeveloper() {
        _uiState.update { it.copy(isAboutDeveloperVisible = false) }
    }

    companion object {
        fun provideFactory(repository: ThemePreferencesRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AgeCalculatorViewModel(repository) as T
                }
            }
        }
    }
}
