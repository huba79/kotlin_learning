# To-Do List App — Business Documentation

## Purpose
A local Android to-do list application allowing users to organize tasks into multiple lists, track completion status, set deadlines with reminder notifications, and customize appearance and language.

## Core Capabilities
- **Multi-list & Item Management**: Create, edit, delete, search, and filter to-do lists (`TODO`, `IN_PROGRESS`, `DONE`), and manage checkable items inside each list with automatic list/item status synchronization.
- **List Card Overview**: Each list card displays its title, status badge, item completion progress bar, read-only due date, and an overflow menu (`Edit`, `Notification Settings`, `Delete`). Tapping a card opens its detail view.
- **Deadlines & Notifications**: Assign or update list deadlines from the Create/Edit List dialog or the list detail screen, configure global and per-list reminder lead times (15m to 24h), and receive system notifications before deadlines expire.
- **Personalization & Localization**: Supports Light/Dark/System theme modes, selectable Material 3 color palettes (`Default`, `Ocean`, `Emerald`), and English (`en`), Hungarian (`hu`), and Romanian (`ro`) languages with immediate in-app language switching.
