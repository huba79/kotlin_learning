# UI and Persistence Fixes Specification

## 1. Dynamic Status Bar Contrast
- **Component**: `PreferencesManager.applyStatusBarColor`
- **Requirement**: Calculate color luminance using `androidx.core.graphics.ColorUtils.calculateLuminance(color)`. If luminance < 0.5 (dark background), appearance light status bars = `false`. If luminance >= 0.5 (light background), appearance light status bars = `true`.

## 2. Room Transaction in Repository & DAO
- **Component**: `TodoListDao` & `TodoListRepository.updateListStatus`
- **Requirement**: Annotate DAO helper or wrap repository update sequence in a `@Transaction` block to ensure `updateList` and `updateAllItemsStatusForList` execute atomically.

## `3. Automatic List Status Synchronization`
- **Component**: `TodoListRepository.updateItemDone`
- **Requirement**: After updating an item's completion status, query or check whether all items in the list are checked (or none are checked, or in-progress), and update the parent list status accordingly (`DONE` if all done, `TODO` if none done).

## 4. Immediate Locale Refresh in SettingsActivity
- **Component**: `SettingsActivity.setupListeners`
- **Requirement**: Call `recreate()` when language tag changes via `PreferencesManager.saveLanguageTag`.
