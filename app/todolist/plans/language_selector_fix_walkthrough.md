# Language Selector Fix Walkthrough

## Progress Checklist
- [x] **Task 1**: Create `language-selector-fix` branch and write spec, plan, and walkthrough in `plans/`
- [x] **Task 2**: Remove premature manual `recreate()` in `SettingsActivity.kt`
- [x] **Task 3**: Guard `PreferencesManager.applySavedPreferencesOnAppLaunch()` against overwriting active `AppCompatDelegate` locales
- [x] **Task 4**: Analyze warnings, run clean build & unit tests, and verify live language switching on the phone

## Changes Implemented
- **`SettingsActivity.kt`**: Removed the premature manual `recreate()` call in `radioGroupLanguage`'s `OnCheckedChangeListener`. `AppCompatDelegate.setApplicationLocales()` already handles Activity recreation once the system/AppCompat locale configuration update is ready, preventing the race condition that caused `SettingsActivity` to re-render with the previous language's strings.
- **`PreferencesManager.kt`**: Guarded `applySavedPreferencesOnAppLaunch()` with `AppCompatDelegate.getApplicationLocales().isEmpty` before applying `KEY_LANGUAGE` from `SharedPreferences`, preserving backward-compatible cold-start restoration on Android 11–12 while avoiding overwriting OS-managed per-app locales on Android 13+.

## Verification Results
- **Static Analysis (`studio_analyze_file`)**: 0 warnings/errors in `SettingsActivity.kt` and `PreferencesManager.kt`.
- **Build & Unit Tests (`clean testDebugUnitTest assembleDebug`)**: Build succeeded; **6 unit tests passed** (0 skipped, 0 failed).
- **Live Device Verification (`adb-RZCW80S7TZW-etjakq._adb-tls-connect._tcp`, Android 16 / API 36)**:
  - Before fix: Selecting `Română (Román)` from Hungarian in `SettingsActivity` recreated the window with `radioLangRo` checked, while all UI labels remained in Hungarian (`"Beállítások"`, `"Téma Mód"`, `"Nyelv"`).
  - After fix: Selecting `English (Engleză)` from Romanian in `SettingsActivity` immediately recreated `SettingsActivity` in English (`"Settings"`, `"Theme Mode"`, `"Color Theme"`, `"Language"`), and pressing Back showed `MainActivity` updated to English (`"My Todo Lists"`, `"To Do"`, `"Due: ..."`).
