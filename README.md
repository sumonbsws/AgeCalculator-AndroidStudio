# Age Calculator — Android (Kotlin + Jetpack Compose + Material 3)

A native Android Age Calculator built with **Kotlin 2.0**, **Jetpack Compose**, **Material 3**, **Dual Calendar Date Pickers** (Date of Birth & Today's Date), **About Developer Profile**, and a **10-Theme Offline Customization System** powered by **Jetpack DataStore Preferences**.

---

## Core Features

1. **Dual Calendar Date Pickers**:
   - **Date of Birth**: Opens a Material 3 `DatePickerDialog` allowing any valid date up to the currently selected **Today's Date**.
   - **Today's Date**: Defaults to `LocalDate.now()` on launch, and opens a Material 3 `DatePickerDialog` allowing any past or future reference date (`1900–2100`) without auto-overwriting user selections during usage.
   - **Reset**: Clears Date of Birth, restores Today's Date to the current system date, clears the result, and preserves the active color theme.
2. **10 Self-Styled Color Themes (Persisted via DataStore)**:
   - Violet Dream (`#6C3CE9` / `#F7F5FF` — Default), Ocean Blue (`#1976D2` / `#F1F7FF`), Emerald Green (`#16865C` / `#F0FAF5`), Rose Pink (`#DB2777` / `#FFF1F7`), Sunset Orange (`#EA580C` / `#FFF7ED`), Midnight Dark (`#A78BFA` / `#15131D`), Teal Fresh (`#0F9690` / `#EFFBFA`), Royal Indigo (`#4338CA` / `#F3F2FF`), Ruby Red (`#DC2626` / `#FFF4F4`), and Golden Amber (`#B7791F` / `#FFFBEB`).
3. **About Developer Screen**:
   - **App Developer:** Md. Zahidul Hasan (Sumon)
   - **Designation:** Computer Engineer
   - **Organization:** Oni IT
   - **Address:** 502 Alpona Plaza, Level-05, 51 New Elephant Road, Dhaka-1205
   - **Contact:** `01841198225` (opens Android phone dialer prefilled via `Intent.ACTION_DIAL` without placing an automatic call or requiring `CALL_PHONE` permission).

---

## Setup & Build Instructions (Android Studio)

1. Extract the `android-age-calculator` project folder.
2. Open **Android Studio Ladybug (2024.2.1+)** and select **File → Open...**, choosing `android-age-calculator`.
3. Allow **Gradle Sync** to finish using bundled **JDK 17**.
4. Select an Android Emulator or physical Android device (API 24–35) and click **Run 'app'**.
