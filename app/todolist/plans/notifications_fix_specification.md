# Deadline Notifications WorkManager Specification

## 1. Overview
Implement reliable background deadline reminder scheduling and notification delivery using Jetpack `WorkManager` so users receive system notifications on their phone when a todo list reaches its configured notification threshold (`deadline - notificationOffsetMinutes`).

## 2. Permissions & Notification Channel
- **Manifest**: Declare `<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />` in `AndroidManifest.xml`.
- **Runtime Permission**: On Android 13+ (API 33+), `MainActivity` requests `Manifest.permission.POST_NOTIFICATIONS` if not already granted.
- **Notification Channel**: Register channel `todo_deadline_reminders` (`IMPORTANCE_HIGH`) with localized channel name and description (`en`, `hu`, `ro`).

## 3. Scheduling Logic & Edge Cases
- **Trigger Timestamp**: `triggerTime = list.deadline - (list.notificationOffsetMinutes * 60_000L)`.
- **Delay Calculation**:
  - If `list.status == ListStatus.DONE`: do not schedule; cancel any pending reminder for `list.id`.
  - If `list.deadline <= now`: do not schedule stale past deadlines; cancel any pending reminder for `list.id`.
  - Otherwise: `delayMs = (triggerTime - now).coerceAtLeast(0L)` (fires immediately if threshold has already passed while deadline is still in the future).
- **Unique Work Naming**: Use `"deadline_reminder_${list.id}"`.
  - Explicit create/edit/reschedule operations enqueue with `ExistingWorkPolicy.REPLACE`.
  - Startup sync for pre-existing Room lists enqueues with `ExistingWorkPolicy.KEEP` (and cancels `DONE`/expired lists).
  - Deleting a list or transitioning its status to `DONE` cancels `"deadline_reminder_${list.id}"`.

## 4. Worker & Notification Payload
- **`DeadlineNotificationWorker` (`CoroutineWorker`)**:
  - Reads `listId` from `inputData`.
  - Queries `TodoListDao.getListWithItemsByIdSync(listId)` on background thread.
  - Exits cleanly without notifying if the list was deleted, is `ListStatus.DONE`, or its `deadline` has already passed.
  - Posts a notification with localized title/body, monochrome status bar icon (`ic_notification_deadline`), and a `PendingIntent` (with back stack) opening `ListDetailActivity` for `listId`.

## 5. Localization
- Add localized strings for notification channel name, channel description, notification title, and notification body in `values/strings.xml`, `values-hu/strings.xml`, and `values-ro/strings.xml`.
