# Todo Card Behavior Fix Walkthrough

## Progress Checklist
- [x] **Task 1**: Create branch `todo-behavior-fix` and write spec, plan, and walkthrough in `plans/`
- [x] **Task 2**: Make `layoutDeadline` read-only in `item_todo_list_card.xml`
- [x] **Task 3**: Remove `onDeadlineClick` and `Postpone / Edit Deadline` menu item from `TodoListAdapter.kt` and `MainActivity.kt`
- [x] **Task 4**: Remove unused `action_change_deadline` strings in `en`, `hu`, and `ro`
- [x] **Task 5**: Analyze warnings, run clean build & unit tests, and verify on device

## Changes Implemented
- **`item_todo_list_card.xml`**:
  - Removed `android:clickable="true"`, `android:focusable="true"`, and `android:background="?attr/selectableItemBackground"` from `layoutDeadline` so the card due date renders as read-only and taps pass through to the card.
  - Increased `progressIndicator` `app:trackThickness` 1.5x (`4dp` -> `6dp`), set `app:trackCornerRadius="3dp"`, and scaled `android:layout_marginTop` proportionally (`8dp` -> `12dp`).
- **`TodoListAdapter.kt`**: Removed `onDeadlineClick`, removed `binding.layoutDeadline.setOnClickListener`, and removed `R.string.action_change_deadline` (`Postpone / Edit Deadline`) from the `btnMenu` `PopupMenu` (keeping `Edit`, `Notification Settings`, and `Delete`).
- **`MainActivity.kt`**: Removed the `onDeadlineClick` callback from `TodoListAdapter` initialization and deleted the unused `showDatePickerDialogForList` method (while preserving deadline editing in `showAddEditListDialog` and `ListDetailActivity`).
- **`values/strings.xml`, `values-hu/strings.xml`, `values-ro/strings.xml`**: Removed the obsolete `action_change_deadline` string across all 3 locales.

## Verification Results
- **Unit Tests & Build (`clean testDebugUnitTest assembleDebug assembleDebugAndroidTest`)**: Build succeeded; `:testDebugUnitTest` ran **6 passed, 0 skipped, 0 failed**.
- **Device Verification (`adb-RZCW80S7TZW-etjakq._adb-tls-connect._tcp`)**: Deployed in-place (`studio_deploy`, preserving existing user list data), confirmed `layoutDeadline` is non-clickable/non-focusable in UI hierarchy, and verified tapping the due date opens `ListDetailActivity` rather than a date picker.
