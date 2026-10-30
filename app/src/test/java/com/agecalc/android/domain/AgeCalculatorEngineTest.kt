package com.agecalc.android.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class AgeCalculatorEngineTest {

    @Test
    fun `verifies Dec 1 1980 to Oct 29 2026 equals 45 years 10 months 28 days`() {
        val birthDate = LocalDate.of(1980, 12, 1)
        val today = LocalDate.of(2026, 10, 29)

        val result = (AgeCalculatorEngine.calculateAge(
            birthDate,
            today
        ) as AgeCalculationOutcome.Success).result

        assertEquals(45, result.years)
        assertEquals(10, result.months)
        assertEquals(28, result.days)
        assertEquals("45 Years, 10 Months, 28 Days", result.formatted)
    }

    @Test
    fun `verifies Jan 1 2000 to Jan 1 2025 equals 25 years 0 months 0 days`() {
        val birthDate = LocalDate.of(2000, 1, 1)
        val today = LocalDate.of(2025, 1, 1)

        val result = (AgeCalculatorEngine.calculateAge(
            birthDate,
            today
        ) as AgeCalculationOutcome.Success).result

        assertEquals("25 Years, 0 Months, 0 Days", result.formatted)
    }

    @Test
    fun `formats standard age strictly as Years, Months, Days`() {
        val birthDate = LocalDate.of(2001, 4, 17)
        val today = LocalDate.of(2026, 10, 29)

        val outcome = AgeCalculatorEngine.calculateAge(birthDate, today)
        assertTrue(outcome is AgeCalculationOutcome.Success)
        val result = (outcome as AgeCalculationOutcome.Success).result

        assertEquals(25, result.years)
        assertEquals(6, result.months)
        assertEquals(12, result.days)
        assertEquals("25 Years, 6 Months, 12 Days", result.formatted)
    }

    @Test
    fun `handles January month borrow without VB6 array index underflow`() {
        val birthDate = LocalDate.of(2000, 12, 25)
        val today = LocalDate.of(2026, 1, 10)

        val outcome = AgeCalculatorEngine.calculateAge(birthDate, today)
        val result = (outcome as AgeCalculationOutcome.Success).result

        assertEquals("25 Years, 0 Months, 16 Days", result.formatted)
    }

    @Test
    fun `handles leap year Feb 29 birthday on non-leap year Feb 28 and Mar 1`() {
        val birthDate = LocalDate.of(2000, 2, 29)

        val onFeb28 = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2025, 2, 28)
        ) as AgeCalculationOutcome.Success).result
        assertEquals("24 Years, 11 Months, 30 Days", onFeb28.formatted)

        val onMar1 = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2025, 3, 1)
        ) as AgeCalculationOutcome.Success).result
        assertEquals("25 Years, 0 Months, 1 Days", onMar1.formatted)
    }

    @Test
    fun `handles leap year Feb 29 birthday on leap year Feb 29`() {
        val birthDate = LocalDate.of(2000, 2, 29)
        val onLeapBirthday = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2024, 2, 29)
        ) as AgeCalculationOutcome.Success).result

        assertEquals("24 Years, 0 Months, 0 Days", onLeapBirthday.formatted)
    }

    @Test
    fun `handles end of month Jan 31 birthday across February and March`() {
        val birthDate = LocalDate.of(2023, 1, 31)

        val onFeb28 = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2023, 2, 28)
        ) as AgeCalculationOutcome.Success).result
        assertEquals("0 Years, 0 Months, 28 Days", onFeb28.formatted)

        val onMar1 = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2023, 3, 1)
        ) as AgeCalculationOutcome.Success).result
        assertEquals("0 Years, 1 Months, 1 Days", onMar1.formatted)
    }

    @Test
    fun `recalculates accurately when reference Today date changes`() {
        val birthDate = LocalDate.of(2000, 1, 1)

        val firstCalc = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2025, 1, 1)
        ) as AgeCalculationOutcome.Success).result
        assertEquals("25 Years, 0 Months, 0 Days", firstCalc.formatted)

        val updatedCalc = (AgeCalculatorEngine.calculateAge(
            birthDate,
            LocalDate.of(2030, 6, 15)
        ) as AgeCalculationOutcome.Success).result
        assertEquals("30 Years, 5 Months, 14 Days", updatedCalc.formatted)
    }

    @Test
    fun `rejects birth dates later than selected Today date and missing birth dates`() {
        val selectedToday = LocalDate.of(2025, 5, 10)
        val laterBirthDate = LocalDate.of(2025, 5, 11)

        assertEquals(
            AgeCalculationOutcome.FutureBirthDate,
            AgeCalculatorEngine.calculateAge(laterBirthDate, selectedToday)
        )
        assertEquals(
            AgeCalculationOutcome.MissingBirthDate,
            AgeCalculatorEngine.calculateAge(null, selectedToday)
        )
    }
}
