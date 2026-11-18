# Deadline Notifications Fix Implementation Plan

## Goal
Wire up Jetpack `WorkManager`, Android 13+ `POST_NOTIFICATIONS` permission handling, `NotificationChannel` setup, and repository lifecycle hooks so approaching-deadline notifications actually fire on the device.

## Resumable Execution Tasks
- **Task 1**: Add `androidx.work:work-runtime-ktx` dependency in `build.gradle` and `POST_NOTIFICATIONS` permission in `AndroidManifest.xml`, then run Gradle sync.
- **Task 2**: Add `ic_notification_deadline.xml` vector icon and localized notification strings (`en`, `hu`, `ro`).
- **Task 3**: Implement `DeadlineReminderScheduler` interface, `NotificationHelper`, `DeadlineNotificationWorker`, and `WorkManagerDeadlineScheduler`.
- **Task 4**: Integrate `DeadlineReminderScheduler` into `TodoListRepository` across all list/item mutations (insert, edit, deadline change, offset change, status change, item completion auto-sync, delete).
- **Task 5**: Wire `NotificationHelper.ensureNotificationChannel`, runtime `POST_NOTIFICATIONS` permission request, `WorkManagerDeadlineScheduler` injection, and initial sync for pre-existing lists in `MainActivity` and `ListDetailActivity`.
- **Task 6**: Add unit tests for reminder delay/edge-case calculations and repository scheduling/cancellation calls, then run clean build, unit tests, lint/warning check, and device verification.
