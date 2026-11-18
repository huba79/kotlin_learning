# To-Do List App — Technical Documentation

## Architecture
- **UI Layer**: ViewBinding-based `AppCompatActivity` screens (`MainActivity`, `ListDetailActivity`, `SettingsActivity`) with RecyclerView `ListAdapter`s (`TodoListAdapter`, `CheckableItemAdapter`) and Material 3 themes.
- **List Card & Interactions**: `item_todo_list_card.xml` renders a read-only due date row (`layoutDeadline`), a `6dp` `LinearProgressIndicator` (`trackCornerRadius="3dp"`, `layout_marginTop="12dp"`), and an overflow menu (`Edit`, `Notification Settings`, `Delete`). Deadline editing lives in `MainActivity.showAddEditListDialog` and `ListDetailActivity`.
- **Data & Persistence**: Room database (`AppDatabase`, schema v2) storing `TodoListEntity` and `TodoItemEntity` (cascade-deleted foreign key) via `TodoListDao` and `TodoListRepository`. User preferences are stored via `PreferencesManager` (`SharedPreferences`).
- **Background Reminders**: `WorkManagerDeadlineScheduler` schedules unique `CoroutineWorker` jobs (`DeadlineNotificationWorker`) via `WorkManager`, triggered by `TodoListRepository` mutations and initial startup sync, posting high-priority notifications via `NotificationHelper` (`todo_deadline_reminders` channel).

## Build & Test
- **Stack**: Kotlin, AGP 8.7.1, Room 2.6.1, WorkManager 2.9.1, Material 1.12.0, `minSdk 30`, `compileSdk 34`, `targetSdk 33`.
- **Verification**: JVM unit tests (`ExampleUnitTest`) and repository instrumentation tests (`TodoListRepositoryTest`).
