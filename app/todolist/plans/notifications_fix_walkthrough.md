# Deadline Notifications Fix Walkthrough

## Progress Checklist
- [x] **Task 1**: Add WorkManager dependency & `POST_NOTIFICATIONS` manifest permission, sync Gradle
- [x] **Task 2**: Add notification vector icon & localized strings (`en`, `hu`, `ro`)
- [x] **Task 3**: Implement `NotificationHelper`, `DeadlineNotificationWorker`, and `WorkManagerDeadlineScheduler`
- [x] **Task 4**: Hook `DeadlineReminderScheduler` into `TodoListRepository`
- [x] **Task 5**: Wire channel creation, runtime permission request, and scheduler injection in `MainActivity` & `ListDetailActivity`
- [x] **Task 6**: Add unit tests, verify clean build & tests, check warnings, and verify on device

## Changes Implemented
- **`build.gradle` & `AndroidManifest.xml`**: Added `androidx.work:work-runtime-ktx:2.9.1` and `<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />`.
- **Resources**: Added `ic_notification_deadline.xml` and localized notification channel/title/body strings across `values/strings.xml`, `values-hu/strings.xml`, and `values-ro/strings.xml`.
- **Notifications Package (`com.example.todolist.notifications`)**:
  - `DeadlineReminderScheduler`: Scheduling/cancellation abstraction for `TodoListRepository`.
  - `NotificationHelper`: Registers the `todo_deadline_reminders` `NotificationChannel`, checks `POST_NOTIFICATIONS`, and builds/posts high-priority notifications opening `ListDetailActivity`.
  - `DeadlineNotificationWorker`: `CoroutineWorker` that verifies the list still exists and is not `ListStatus.DONE` before posting the notification.
  - `WorkManagerDeadlineScheduler`: Computes `(deadline - offset) - now`, cancels `DONE`/past-deadline items, clamps past-threshold future deadlines to `0L`, and manages unique per-list jobs (`deadline_reminder_<id>`).
- **Repository & Activities**:
  - `TodoListRepository`: Schedules/reschedules/cancels reminders on `insertList`, `updateListDetails`, `updateListDeadline`, `updateNotificationOffset`, `updateListStatus`, `updateItemDone`, and `deleteList`.
  - `MainActivity` & `ListDetailActivity`: Ensure channel creation, request `POST_NOTIFICATIONS` on Android 13+, inject `WorkManagerDeadlineScheduler`, and sync pre-existing active lists on startup.

## Verification Results
- **Unit Tests (`clean testDebugUnitTest assembleDebug`)**: 6 passed, 0 skipped, 0 failed; clean build succeeded.
- **Instrumentation Tests (`connectedDebugAndroidTest`)**: 4 passed on connected device (`adb-RZCW80S7TZW-etjakq._adb-tls-connect._tcp`).
- **Live Device Verification**: Created a test list and configured `24 hours before` on a +24h deadline; verified `NotificationRecord` posted to channel `todo_deadline_reminders` (`Upcoming deadline: ...`, `importance=4`, `numPostedByApp=1`) via `dumpsys notification`, then cleaned up the test list.
