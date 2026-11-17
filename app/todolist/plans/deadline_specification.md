# Todo List Deadlines & Notification Timers Specification

## 1. Data Model
- **`TodoListEntity`**:
  - `deadline`: `Long` (Epoch milliseconds). Default value = `System.currentTimeMillis() + 24 * 60 * 60 * 1000L` (+1 day).
  - `notificationOffsetMinutes`: `Int` (Minutes before deadline to trigger notification). Default value = `60` (1 hour).

## 2. UI & Interaction
- **List Card**: Shows formatted deadline string (`MMM dd, yyyy HH:mm`) below progress or next to status chip.
- **Add/Edit Dialog**: Includes interactive Date and Time pickers allowing custom deadline selection.
- **Notification Configurator (Breadcrumb / Overflow Menu)**:
  - Accessible from `MainActivity` (sets global default in `PreferencesManager`) and per-list menu (overrides for specific list).
  - Dialog presents selectable duration options: 15 minutes, 30 minutes, 1 hour (default), 2 hours, 6 hours, 24 hours.

## 3. Notifications
- Background worker (`WorkManager`) checks or schedules alarms/notifications before the deadline minus `notificationOffsetMinutes`.
