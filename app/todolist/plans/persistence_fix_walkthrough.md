# Persistence & List Display Fix Walkthrough

## Progress Checklist
- [x] **Task 1**: Document specification, implementation plan, and resumable walkthrough in `plans/`
- [x] **Task 2**: Update `AppDatabase.kt` with `MIGRATION_1_2` and remove `.fallbackToDestructiveMigration()`
- [x] **Task 3**: Fix `MainActivity.loadData()` collector cancellation and redundant query restarts
- [x] **Task 4**: Add unit and migration tests (`ExampleUnitTest.kt`, `AppDatabaseMigrationTest.kt`)
- [x] **Task 5**: Analyze warnings and run clean build, unit tests, and AndroidTest compilation

## Changes Implemented
- **`AppDatabase.kt` (Fix 1)**:
  - Added explicit `MIGRATION_1_2` (`Migration(1, 2)`) that adds `deadline` (populated with +24h default for legacy v1 rows) and `notificationOffsetMinutes` (default `60`) to `todo_lists` while preserving all existing lists and items.
  - Registered `MIGRATION_1_2` via `.addMigrations(MIGRATION_1_2)` and removed `.fallbackToDestructiveMigration()` so future schema bumps cannot silently wipe user data.
- **`MainActivity.kt` (Fix 2)**:
  - Added `loadDataJob: Job?` and cancelled any active collector job at the start of `loadData()` before launching a new `lifecycleScope.launch` collector.
  - Guarded `setupSearchAndFilter()` listeners so `loadData()` is only re-invoked when the trimmed search query or selected `ListStatus` filter actually changes.
- **Tests**:
  - `ExampleUnitTest.kt`: Added `testAppDatabaseMigration1To2_bounds`.
  - `AppDatabaseMigrationTest.kt`: Added instrumentation test `migrate1To2_preservesExistingListsAndItems` using a real SQLite v1 schema and sample data migrated to Room v2.

## Verification Results
- **Unit Tests & Build (`clean testDebugUnitTest assembleDebug assembleDebugAndroidTest`)**: Build succeeded; `:testDebugUnitTest` ran **7 passed, 0 skipped, 0 failed**; `assembleDebugAndroidTest` compiled `AppDatabaseMigrationTest` cleanly.
- **Instrumentation Execution**: Intentionally not executed on the connected physical phone (`adb-RZCW80S7TZW-etjakq._adb-tls-connect._tcp`) because AGP's `connectedDebugAndroidTest` uninstalls `com.example.todolist` on teardown and wipes device data (and no Android emulator is currently configured on the host).
