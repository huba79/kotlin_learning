# Todo Card Behavior Fix Implementation Plan

## Goal
Make the due date read-only on `item_todo_list_card.xml` and remove the `Postpone / Edit Deadline` option from `btnMenu` on branch `todo-behavior-fix`, while preserving deadline editing in the Edit List dialog and `ListDetailActivity`.

## Resumable Execution Tasks
- **Task 1**: Create branch `todo-behavior-fix` and write specification, implementation plan, and resumable walkthrough in `plans/`.
- **Task 2**: Make `layoutDeadline` read-only in `item_todo_list_card.xml`.
- **Task 3**: Remove `onDeadlineClick`, `layoutDeadline` click listener, and `action_change_deadline` menu item from `TodoListAdapter.kt`, and remove the unused callback/helper from `MainActivity.kt`.
- **Task 4**: Remove unused `action_change_deadline` strings across `en`, `hu`, and `ro` resources.
- **Task 5**: Analyze modified files for warnings, run clean build and unit tests (`clean testDebugUnitTest assembleDebug`), and verify visually on device/layout.
