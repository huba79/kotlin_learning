# UI and Persistence Fixes Implementation Plan

## Goal
Implement fixes for four identified code review issues:
1. **Dynamic Status Bar Contrast**: Update `PreferencesManager.applyStatusBarColor` to calculate background color luminance and set `isAppearanceLightStatusBars` accordingly.
2. **Room Transaction Wrapper**: Wrap multi-step list status and item updates in a Room transaction in `TodoListRepository` and `TodoListDao` to guarantee atomic consistency.
3. **Immediate Settings Language Refresh**: Call `recreate()` in `SettingsActivity` when the language preference is changed so UI strings update immediately.
4. **Automatic List Status Synchronization**: Update `TodoListRepository.updateItemDone` to check if all items in a list are completed/uncompleted and automatically synchronize the parent list's status (`DONE` / `TODO`).

## Tasks
- [ ] Task 1: Create Fix Specification & Walkthrough Tracking
- [ ] Task 2: Implement Dynamic Status Bar Contrast in `PreferencesManager`
- [ ] Task 3: Add Room Transaction Support for List Status Updates in DAO & Repository
- [ ] Task 4: Implement Automatic Parent List Status Synchronization on Item Check/Uncheck
- [ ] Task 5: Add Activity Recreation on Language Change in `SettingsActivity`
- [ ] Task 6: Run Unit & Instrumentation Tests and Build Verification
