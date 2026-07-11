# Ledger

[中文版](./README_zh.md)

Personal Android accounting app focused on privacy and local-first data storage.

Every balance holder — cash, bank cards, WeChat Pay, Alipay, transit cards, store cards, and more — is modeled as an **account**. Transfers between accounts are first-class, so real-world payment paths stay accurate without polluting income/expense stats.

App display name: **Ledger** (`com.dodo.accounting`).

## Screens

| Tab | Role |
|-----|------|
| **Home** | Period summary (week / month / year / custom) and day-grouped transactions |
| **Stats** | Analysis: category ranking, trends, calendar, filtered flow |
| **Assets** | Net worth overview, account balances, quick transfer |
| **Settings** | Accounts, categories/tags, budget & recurring rules, backup, preferences, trash |

The center **+** button opens quick entry. **Long-press** starts voice entry (when enabled).

## Key Features

### Accounts & transactions

- **Asset accounts**: name, icon, color, initial balance, archive, reorder
- **Expense / Income**: category required; optional note and tags
- **Transfer**: from → to accounts; excluded from income/expense statistics
- **Quick entry sheet**: amount keypad with expressions (`12+3.5`), date/time, tags, “save and continue”
- **Voice entry**: system speech recognition → parse amount, type, account, category (depends on device speech engine)
- **Soft delete**: transactions go to trash; restore or purge permanently

> **Note:** Balance adjustment is **no longer offered for new entries**. Legacy adjustment rows still appear in history (read-only / deletable to trash).

### Budget & planning

- **Monthly total budget** and **per-category budgets**
- Budget hints while recording an expense against a category
- **Recurring rules** (monthly interval): auto-generate due bills/income when the app runs generation

### Categories & tags

- Separate expense / income categories with icon and color
- Reorder and manage in Settings
- Tags for cross-cutting labels; optional “common category / tag first” suggestions from recent usage

### Statistics

- Range: week / month / year / custom
- Views: **category ranking**, **trend**, **calendar**, **flow**
- Filters: type, accounts, categories, tags, date range
- Transfers and (legacy) balance adjustments are excluded from income/expense totals

### Data & privacy

- **Local-first**: no cloud account; Room database on device
- **JSON** full backup import/export (with import preview)
- **CSV** transaction export
- **WebDAV** upload/download of JSON backups (optional; credentials stored in app preferences)
- **Amount hide** toggle (privacy mask on amounts)
- **Theme**: light / dark / follow system
- Entry defaults: default type & account, current time, continue after save, voice on/off

## Tech Stack

| Layer | Choice |
|-------|--------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| DB | Room |
| DI | Hilt |
| Async | Kotlin Flow + ViewModel |
| Serialization | kotlinx.serialization (backup) |

## Project layout (main)

```
app/src/main/java/com/dodo/accounting/
├── data/           # Room entities, DAOs, repository, seed data
├── domain/         # Drafts, rules, money, use cases
├── di/             # Hilt modules
└── ui/
    ├── screen/     # Tabs, entry sheet, stats, settings, voice parsers
    ├── theme/      # Colors, typography
    └── viewmodel/  # AccountingViewModel + action handlers
```

## Requirements

- Android Studio (recent stable)
- Android SDK **35**
- **Java 17**
- Gradle **8.x**
- **minSdk 26**

## Build & test

```powershell
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Debug builds use application id suffix `.debug` so they can sit beside a release install.

## Version

- `versionName`: 1.0.0  
- `versionCode`: 1  

## License / contribution

Personal project. Feel free to fork and adapt for your own local-first ledger needs.
