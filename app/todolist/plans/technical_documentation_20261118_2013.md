# To-Do List App — Technical Documentation

## Architecture
- **UI Layer**: ViewBinding-based `AppCompatActivity` screens (`MainActivity`, `ListDetailActivity`, `SettingsActivity`) with RecyclerView `ListAdapter`s (`TodoListAdapter`, `CheckableItemAdapter`) and Material 3 themes.
- **Data & Persistence**: Room database (`AppDatabase`, schema v2) storing `TodoListEntity` and `TodoItemEntity` (cascade-deleted foreign key) via `TodoListDao` and `TodoListRepository`. Schema upgrades from v1 to v2 are handled explicitly by `AppDatabase.MIGRATION_1_2` without destructive migration fallback. User preferences (theme, palette, language, default reminder offset) are stored via `PreferencesManager` (`SharedPreferences`).
- **Reactive Querying**: `MainActivity` observes Room `Flow` streams via a single cancellable `loadDataJob`, preventing concurrent stale collectors when search queries or status filters change.
- **Background Reminders**: `WorkManagerDeadlineScheduler` schedules unique `CoroutineWorker` jobs (`DeadlineNotificationWorker`) via `WorkManager`, triggered by `TodoListRepository` mutations and initial app startup sync, posting high-priority notifications through `NotificationHelper` (`todo_deadline_reminders` channel).

## Build & Test
- **Stack**: Kotlin, AGP 8.7.1, Room 2.6.1, WorkManager 2.9.1, Material 1.12.0, `minSdk 30`, `compileSdk 34`, `targetSdk 33`.
- **Verification**: JVM unit tests (`ExampleUnitTest`) and Room/repository instrumentation tests (`TodoListRepositoryTest`, `AppDatabaseMigrationTest`).
