# Multi-List Support, Material 3, Settings & Splash Screen Implementation Plan

## Goal
Rework the `todolist` project to support:
1. Multiple Todo lists stored in a **Room SQLite Database**. Each list features a title, status (`ToDo`, `InProgress`, `Done`), and checkable items. List status changes automatically synchronize item check states.
2. Full **Material Design 3** theming with dynamic color palette selection (**Default/Purple**, **Ocean/Blue**, **Emerald/Green**) and Day/Night mode.
3. Multi-language support (**English**, **Hungarian**, **Romanian**) configured with Android 13+ per-app locales.
4. Redesigned, visually appealing application icon featuring adaptive icon layers and Android 13+ `<monochrome>` themed icon support.
5. Splash screen with `androidx.core:core-splashscreen` ensuring at least 1-second display duration or until initial database loading completes.

## Architectural & Design Decisions
1. **Persistence**: Room SQLite Database (`TodoListEntity`, `TodoItemEntity`, `TodoListWithItems` relation).
2. **Search & Filter**: Combined title search bar and status filter chips (`All`, `ToDo`, `In Progress`, `Done`) on `MainActivity`.
3. **Preferences & Settings**: `PreferencesManager` managing Night Mode (`AppCompatDelegate.setDefaultNightMode`), Color Theme (`activity.setTheme(...)`), and Locale (`AppCompatDelegate.setApplicationLocales`).
4. **App Icon & Theming Analysis (Critical Evaluation)**:
   - Modern adaptive icon with layered foreground clipboard vector, clip accent, and distinct checkmarks.
   - Android 13+ (API 33+) native `<monochrome>` support in `res/mipmap-anydpi-v26/ic_launcher.xml` and `ic_launcher_round.xml` enables the OS launcher to dynamically tint the icon to match the system's Material You wallpaper theme.
   - *Critical Architecture Note*: Runtime dynamic icon switching to match in-app custom palette (Ocean/Emerald) via `<activity-alias>` causes the OS to kill/restart the running app process upon component state toggling and can cause launcher icon flickering/disappearance on some OEM home screens. Therefore, standard adaptive + monochrome themed launcher icon is the recommended, stable approach, while in-app headers and the splash screen follow the selected theme palette natively.
5. **Splash Screen**: `androidx.core:core-splashscreen` with `Theme.App.Starting` declared in manifest, configured in `values/themes.xml` and `values-night/themes.xml`. In `MainActivity`, `installSplashScreen().setKeepOnScreenCondition` guarantees display for at least 1000ms or until initial data loading finishes.
6. **Testing**: Unit tests in `ExampleUnitTest` covering theme keys, status enums, and splash screen delay/readiness conditions, along with Room repository tests in `TodoListRepositoryTest`.

## Execution Tasks
- Task 1: Room Database & Multi-List Architecture
- Task 2: Material Design 3 Theming & Color Palettes (Default, Ocean, Emerald)
- Task 3: Localization (en, hu, ro) & Settings Screen
- Task 4: UI & Dialog Modernization (Rounded corners, MaterialAlertDialogBuilder)
- Task 5: Visually Appealing App Icon & Android 13+ Themed Icon Support
- Task 6: Splash Screen Implementation (1-second minimum / load condition)
- Task 7: Unit Testing & Verification
