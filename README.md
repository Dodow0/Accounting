# Accounting

[中文版](./README_zh.md)

Personal Android accounting app built with a focus on privacy and local-first data storage.

The app uses a unified asset management model where every balance holder (Cash, Bank Cards, WeChat Pay, Alipay, Apple Balance, Transit Cards, etc.) is treated as an account. This allows for precise tracking of transfers and real-world spending patterns.

## Key Features

- **Asset Account Management**: Track balances across various account types (Cash, Bank, Third-party, etc.).
- **Flexible Transaction Types**:
    - **Expense/Income**: Standard cash flow tracking.
    - **Transfer**: Record moving money between accounts (excluded from income/expense stats).
    - **Balance Adjustment**: Easily reconcile digital balances with real-world amounts.
- **Budgeting System**:
    - **Monthly Budget**: Set a total spending limit for the month.
    - **Category Budgets**: Set specific limits for different spending categories (e.g., Food, Transport).
- **Periodic Transactions**: Set up monthly recurring rules to automate regular bills and income.
- **Category & Tag System**:
    - Fully customizable categories with icon and color selection.
    - Tagging system for cross-category analysis.
- **Statistics & Insights**:
    - Weekly, monthly, and yearly summaries.
    - Category breakdown charts and monthly spending trends.
- **Data Security & Portability**:
    - **Local-first**: All data stays on your device.
    - **Export/Import**: Backup your data in JSON format or export to CSV for external analysis.
    - **Trash Bin**: Soft-delete protection for all transactions.
- **Modern UI**: Built entirely with Jetpack Compose using Material 3 design principles.

## Tech Stack

- **Kotlin**: Language
- **Jetpack Compose**: UI Framework
- **Room**: Local Database
- **Hilt**: Dependency Injection
- **Kotlin Flow**: Reactive Data Streams
- **ViewModel**: State Management

## Run

Open the project in Android Studio. Ensure you have the latest stable version.

### Build Requirements
- Android SDK 35
- Java 17
- Gradle 8.x

### Quick Checks
```powershell
./gradlew testDebugUnitTest
./gradlew assembleDebug
```
