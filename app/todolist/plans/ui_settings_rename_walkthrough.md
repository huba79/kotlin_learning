# UI Settings & Main Title Rename Walkthrough

## Progress Checklist
- [x] **Task 1**: Create `ui-settings-rename` branch and write spec, plan, and walkthrough in `plans/`
- [x] **Task 2**: Update `action_settings` and `title_settings` across `en`, `hu`, and `ro` string resources
- [x] **Task 3**: Add `title_main` across `en`, `hu`, and `ro` and update `activity_main.xml` + `layout-land/activity_main.xml`
- [x] **Task 4**: Analyze files, run clean build & unit tests, and verify visually on the phone

## Changes Implemented
- **`values/strings.xml`**: Renamed `action_settings` and `title_settings` to `UI Settings`, and added `title_main` = `My Lists` (leaving `app_name` as `My Todo Lists`).
- **`values-hu/strings.xml`**: Renamed `action_settings` and `title_settings` to `Felületi beállítások`, and added `title_main` = `Listáim` (leaving `app_name` as `Teendőim`).
- **`values-ro/strings.xml`**: Renamed `action_settings` and `title_settings` to `Setări interfață`, and added `title_main` = `Listele mele` (leaving `app_name` as `Listele mele Todo`).
- **`activity_main.xml` & `layout-land/activity_main.xml`**: Updated `topAppBar`'s `app:title` to `@string/title_main`.

## Verification Results
- **Static Analysis (`studio_analyze_file`)**: 0 warnings/errors across all three `strings.xml` files and both `activity_main.xml` layouts.
- **Build & Unit Tests (`clean testDebugUnitTest assembleDebug`)**: Succeeded (**6 passed, 0 skipped, 0 failed**).
- **Device Verification (`adb-RZCW80S7TZW-etjakq._adb-tls-connect._tcp`)**: Verified `MainActivity` toolbar shows `Listáim` (Hungarian active locale) while preserving launcher `app_name`, and overflow menu / settings toolbar show `UI Settings` / `Felületi beállítások`.
