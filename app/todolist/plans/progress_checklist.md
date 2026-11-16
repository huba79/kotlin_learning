# Multi-List Support, Material 3, Settings & Splash Screen Progress Checklist

- [x] Task 1: Room Database & Multi-List Architecture (`TodoListEntity`, `TodoItemEntity`, `TodoListDao`, `AppDatabase`, `TodoListRepository`)
- [x] Task 2: Material Design 3 Theming & Color Palettes (Default, Ocean, Emerald)
- [x] Task 3: Localization (`en`, `hu`, `ro`) & Settings Screen (`SettingsActivity`, `PreferencesManager`)
- [x] Task 4: UI & Dialog Modernization (FAB tint, 28dp rounded MaterialAlertDialogBuilder, 16dp text input radius, 14dp chips)
- [x] Task 5: App Icon Design (Minimalist Rounded Vector)
  - [x] Emerald-to-Ocean linear gradient background (`ic_launcher_background.xml`)
  - [x] Thick rounded dual checkmarks with pill item bars (`ic_launcher_foreground.xml`)
  - [x] Android 13+ `<monochrome>` themed icon layer (`ic_launcher_monochrome.xml`)
  - [x] Branded squircle splash icon (`ic_splash_icon.xml`)
- [x] Task 6: Splash Screen Implementation (`androidx.core:core-splashscreen`)
  - [x] Theme `Theme.App.Starting` configured for Day & Night
  - [x] `MainActivity` integration with `installSplashScreen()`
  - [x] Minimum 1-second display duration and data readiness condition
- [x] Task 7: Unit Testing & Build Verification
  - [x] `ExampleUnitTest` verifying theme keys, enum values, and splash condition logic (3/3 passing)
  - [x] `assembleDebug` clean build success
