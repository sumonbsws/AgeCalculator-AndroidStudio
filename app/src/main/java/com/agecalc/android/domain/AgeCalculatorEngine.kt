package com.agecalc.android.domain

import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class AgeResult(
    val years: Int,
    val months: Int,
    val days: Int,
    val totalDays: Long
) {
    /**
     * Formats completed age strictly as: "25 Years, 6 Months, 12 Days"
     */
    val formatted: String
        get() = "$years Years, $months Months, $days Days"
}

sealed interface AgeCalculationOutcome {
    data class Success(val result: AgeResult) : AgeCalculationOutcome
    data object MissingBirthDate : AgeCalculationOutcome
    data object FutureBirthDate : AgeCalculationOutcome
}

object AgeCalculatorEngine {

    private val displayFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US)

    /**
     * Calculates exact completed years, months, and days between [birthDate] and [today].
     *
     * Fixes original VB6/VBA AgeCalc.frm issues:
     * 1. Properly handles Gregorian leap years (divisible by 4, except centuries not divisible by 400).
     * 2. Fixes January array index underflow (`Days_of_Months(Month(Today) - 2)` when Month = 1).
     * 3. Accurately handles end-of-month birthdays (e.g., Jan 31, Feb 29, Mar 31, May 31).
     * 4. Rejects future birth dates cleanly.
     */
    fun calculateAge(
        birthDate: LocalDate?,
        today: LocalDate = LocalDate.now()
    ): AgeCalculationOutcome {
        if (birthDate == null) {
            return AgeCalculationOutcome.MissingBirthDate
        }
        if (birthDate.isAfter(today)) {
            return AgeCalculationOutcome.FutureBirthDate
        }

        val period = Period.between(birthDate, today)
        val totalDays = ChronoUnit.DAYS.between(birthDate, today)

        return AgeCalculationOutcome.Success(
            AgeResult(
                years = period.years,
                months = period.months,
                days = period.days,
                totalDays = totalDays
            )
        )
    }

    /**
     * Material 3 DatePicker stores timestamps at 00:00:00 UTC.
     * Converting with ZoneOffset.UTC prevents off-by-one-day shifts in negative UTC offsets.
     */
    fun utcMillisToLocalDate(utcMillis: Long): LocalDate {
        return Instant.ofEpochMilli(utcMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
    }

    fun localDateToUtcMillis(date: LocalDate): Long {
        return date.atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    }

    fun formatDisplayDate(date: LocalDate): String {
        return date.format(displayFormatter)
    }
}
