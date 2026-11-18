# Language Selector Immediate Update Specification

## 1. Overview
Fix the Settings screen language selector so changing the app language (`en`, `hu`, `ro`) updates `SettingsActivity` immediately without showing stale translated strings, and keep `MainActivity` synchronized when returning from Settings or changing per-app language via system settings.

## 2. Root Cause
- On Android 13+ (API 33+), `AppCompatDelegate.setApplicationLocales()` delegates asynchronously to `LocaleManager` and automatically triggers Activity recreation once the new locale configuration is applied.
- `SettingsActivity` called `recreate()` synchronously immediately after `PreferencesManager.saveLanguageTag()`, racing with `LocaleManager` and recreating `SettingsActivity` before the new locale took effect.
- Additionally, `PreferencesManager.applySavedPreferencesOnAppLaunch()` unconditionally re-applied `KEY_LANGUAGE` from `SharedPreferences` on every `MainActivity.onCreate()`, which could override Android 13+ system per-app language settings or re-trigger `setApplicationLocales` unnecessarily.

## 3. Required Behavior
- **`SettingsActivity`**: Do not call `recreate()` manually after `PreferencesManager.saveLanguageTag(this, langTag)`; allow `AppCompatDelegate.setApplicationLocales()` to handle locale-aware Activity recreation cleanly.
- **`PreferencesManager`**:
  - In `applySavedPreferencesOnAppLaunch()`, only call `AppCompatDelegate.setApplicationLocales()` from `SharedPreferences` when `AppCompatDelegate.getApplicationLocales().isEmpty` (cold start on API <= 32 or initial migration), avoiding overwriting active OS per-app locales on API 33+.
  - In `getLanguageTag()`, read the active `AppCompatDelegate.getApplicationLocales()` first and fall back to `SharedPreferences` / default `"en"`.
