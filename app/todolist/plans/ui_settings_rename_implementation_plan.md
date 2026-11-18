# UI Settings & Main Title Rename Implementation Plan

## Goal
Rename `Settings` to `UI Settings` (menu + `SettingsActivity` toolbar) and rename the `MainActivity` toolbar title to `My Lists` across `en`, `hu`, and `ro` on branch `ui-settings-rename`.

## Resumable Execution Tasks
- **Task 1**: Create `ui-settings-rename` branch and write spec, implementation plan, and resumable walkthrough in `plans/`.
- **Task 2**: Update `action_settings` and `title_settings` in `values/strings.xml`, `values-hu/strings.xml`, and `values-ro/strings.xml`.
- **Task 3**: Add `title_main` across `en`, `hu`, and `ro` and wire it in `activity_main.xml` and `layout-land/activity_main.xml`.
- **Task 4**: Run lint/file analysis, clean build & unit tests (`clean testDebugUnitTest assembleDebug`), and verify visually on the connected device.
