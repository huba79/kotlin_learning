# Language Selector Fix Implementation Plan

## Goal
Ensure selecting a language in `SettingsActivity` updates the UI immediately on `SettingsActivity` and across the app on branch `language-selector-fix`.

## Resumable Execution Tasks
- **Task 1**: Create `language-selector-fix` branch and write spec, implementation plan, and resumable walkthrough in `plans/`.
- **Task 2**: Remove the premature manual `recreate()` call in `SettingsActivity`'s `radioGroupLanguage` listener.
- **Task 3**: Guard `PreferencesManager.applySavedPreferencesOnAppLaunch()` so it only restores saved locale from `SharedPreferences` when `AppCompatDelegate.getApplicationLocales().isEmpty`.
- **Task 4**: Add unit tests for locale/language preference fallback logic if applicable, analyze warnings, run clean build & unit tests (`clean testDebugUnitTest assembleDebug`), and verify immediate language switching live on the connected phone.
