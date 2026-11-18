# Persistence & List Display Fix Implementation Plan

## Goal
Implement explicit Room v1 -> v2 migration without destructive fallback (Fix 1) and prevent stale concurrent `Flow` collectors in `MainActivity` (Fix 2) on branch `persistence-fix`.

## Resumable Execution Tasks
- **Task 1**: Document specification, implementation plan, and resumable walkthrough in `plans/`.
- **Task 2**: Update `AppDatabase.kt` to define `MIGRATION_1_2`, register it via `.addMigrations(MIGRATION_1_2)`, and remove `.fallbackToDestructiveMigration()`.
- **Task 3**: Update `MainActivity.kt` to track and cancel `loadDataJob` in `loadData()` and skip redundant reloads when search query or filter status is unchanged.
- **Task 4**: Add unit tests in `ExampleUnitTest.kt` and Room migration verification test in `AppDatabaseMigrationTest.kt` (`androidTest`), plus verify `TodoListRepositoryTest` compiles cleanly.
- **Task 5**: Run `studio_analyze_file` on touched files, execute `clean testDebugUnitTest assembleDebug assembleDebugAndroidTest`, and prepare the summary/warnings report.
