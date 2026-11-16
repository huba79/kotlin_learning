# UI and Persistence Fixes Walkthrough

## Progress Tracking
- [x] **Dynamic Status Bar Contrast**: Completed in `PreferencesManager.kt` (computes luminance and sets `isAppearanceLightStatusBars`).
- [x] **Room Transaction Wrapper**: Completed in `TodoListDao.kt` & `TodoListRepository.kt` (atomic list and item updates via `@Transaction`).
- [x] **Automatic List Status Synchronization**: Completed in `TodoListRepository.kt` (auto-syncs parent list status on item check/uncheck).
- [x] **Immediate Locale Refresh**: Completed in `SettingsActivity.kt` (`recreate()` invoked on language preference change).
- [x] **Verification**: Clean build & unit test pass
