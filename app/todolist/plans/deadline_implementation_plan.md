# Todo List Deadlines & Notification Timers Implementation Plan

## Goal
Add persisted date and time deadlines for each todo list (defaulting to system time + 1 day) with UI display, and a notification settings configurator (accessible via breadcrumb/overflow menu) supporting selectable notification offsets (minutes or hours before deadline, default 1 hour). Per-list settings take priority over global app-wide defaults.

## Architectural & Design Decisions
1. **Persistence (`TodoListEntity`)**:
   - Add `deadline: Long` (epoch millis, default `System.currentTimeMillis() + 86400000L`).
   - Add `notificationOffsetMinutes: Int` (default `60`).
   - Update `AppDatabase` version and migration strategy (adding columns with default values).
2. **UI Enhancements**:
   - **List Card (`item_todo_list_card.xml` & `TodoListAdapter`)**: Display formatted deadline date and time (e.g., "Due: Nov 17, 2026, 22:30").
   - **Add/Edit List Dialog (`dialog_add_edit_list.xml` & `MainActivity`)**: Add Material DatePicker and TimePicker inputs for editing the list deadline.
   - **Notification Configurator**: Add a menu item in the toolbar / breadcrumb menu (and per-list options menu) to open a dialog configuring notification offset (15m, 30m, 1h, 2h, 6h, 24h).
3. **Notifications**:
   - Use `WorkManager` (or AlarmManager / Alarm permission flow) to schedule approaching deadline notifications.
4. **Testing**:
   - Unit tests covering default deadline calculation, offset selection, and Room entity migration/repository updates.

## Execution Tasks
- Task 1: Update `TodoListEntity` & Room Database Schema / Migration
- Task 2: Implement Deadline Formatting & UI Display on List Cards
- Task 3: Add Date & Time Picker to Add/Edit List Dialog
- Task 4: Implement Notification Settings Configurator (Global & Per-List Priority)
- Task 5: Schedule Approaching Deadline Notifications via WorkManager
- Task 6: Unit Testing & Build Verification
