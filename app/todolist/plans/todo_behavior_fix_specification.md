# Todo Card Behavior Fix Specification

## 1. Overview
Simplify list card interactions in the main list overview (`item_todo_list_card.xml` / `TodoListAdapter`) so the due date on each card is read-only and the `Postpone / Edit Deadline` overflow menu action is removed.

## 2. Card UI & Interaction Changes
- **`item_todo_list_card.xml`**:
  - Remove interactive attributes (`android:clickable="true"`, `android:focusable="true"`, `android:background="?attr/selectableItemBackground"`) from `layoutDeadline` so the due date displays as read-only text and tapping it delegates normally to the parent card (`ListDetailActivity`).
  - Increase `progressIndicator` `app:trackThickness` by 1.5x from Material's `4dp` default to `6dp` (with `app:trackCornerRadius="3dp"`) and scale `android:layout_marginTop` proportionally from `8dp` to `12dp`.
- **`TodoListAdapter` & `MainActivity`**:
  - Remove `binding.layoutDeadline.setOnClickListener` and the `onDeadlineClick` callback from `TodoListAdapter`.
  - Remove `R.string.action_change_deadline` (`Postpone / Edit Deadline`) from the `btnMenu` `PopupMenu` in `TodoListAdapter`, keeping `Edit`, `Notification Settings`, and `Delete`.
  - Remove the unused `showDatePickerDialogForList` helper from `MainActivity`.
- **Preserved Deadline Editing**:
  - Deadline editing remains available via the card menu's `Edit` dialog (`MainActivity.showAddEditListDialog`) and inside `ListDetailActivity` (`btnEditDeadline`).

## 3. Localization Cleanup
- Remove the obsolete `action_change_deadline` string resource in sync across `values/strings.xml`, `values-hu/strings.xml`, and `values-ro/strings.xml`.
