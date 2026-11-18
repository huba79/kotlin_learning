# Persistence & List Display Fix Specification

## 1. Overview
Harden local Room data persistence and reactive list collection so existing user lists are never silently wiped during database upgrades or overwritten by stale concurrent Flow collectors during search and filtering.

## 2. Explicit Room Migration Strategy (`AppDatabase`)
- **Remove silent destructive migration**: Remove `.fallbackToDestructiveMigration()` from `AppDatabase.getDatabase(context)`.
- **Explicit v1 -> v2 migration (`MIGRATION_1_2`)**:
  - Add `deadline INTEGER NOT NULL DEFAULT 0` to `todo_lists` and populate existing rows with `(CAST(strftime('%s', 'now') AS INTEGER) * 1000) + 86400000` (+24h default deadline).
  - Add `notificationOffsetMinutes INTEGER NOT NULL DEFAULT 60` to `todo_lists`.
  - Preserve all existing `todo_lists` and `todo_items` rows across schema v1 -> v2 upgrades.
  - Future schema version bumps without an explicit `Migration` must fail fast rather than silently dropping user data.

## 3. Single Active List Collector (`MainActivity`)
- **Cancel superseded collectors**: Track the active `Job` (`loadDataJob`) for `MainActivity.loadData()` and cancel any previous `loadDataJob` before starting a new `lifecycleScope.launch` collector.
- **Avoid redundant restarts**: In `setupSearchAndFilter()`, only restart `loadData()` when the normalized `searchQuery` or `filterStatus` actually changes.
- **Result**: At most one Room `Flow` collector updates `TodoListAdapter` and empty-state visibility at any time, eliminating out-of-order emissions from stale search/filter queries.

## 4. Verification Safety
- Avoid running `connectedDebugAndroidTest` against a physical device holding live user data (AGP's test runner uninstalls `com.example.todolist` on teardown).
- Compile instrumentation tests with `assembleDebugAndroidTest` and verify unit tests with `clean testDebugUnitTest assembleDebug`.
