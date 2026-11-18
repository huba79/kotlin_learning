# UI Settings & Main Title Rename Specification

## 1. Overview
- Rename the generic `Settings` entry in the main overflow menu (and its matching `SettingsActivity` toolbar title) to `UI Settings` across all supported locales so it is visually distinct from `Notification Settings`.
- Rename the `MainActivity` toolbar title from `My Todo Lists` to `My Lists` (via a dedicated `title_main` string resource) while keeping the launcher icon label (`app_name`) unchanged.

## 2. Localized Strings
- **English (`values/strings.xml`)**:
  - `action_settings` and `title_settings` -> `UI Settings`
  - `title_main` -> `My Lists`
- **Hungarian (`values-hu/strings.xml`)**:
  - `action_settings` and `title_settings` -> `Felületi beállítások`
  - `title_main` -> `Listáim`
- **Romanian (`values-ro/strings.xml`)**:
  - `action_settings` and `title_settings` -> `Setări interfață`
  - `title_main` -> `Listele mele`

## 3. Layouts
- `layout/activity_main.xml` and `layout-land/activity_main.xml`: Point `topAppBar`'s `app:title` to `@string/title_main`.
