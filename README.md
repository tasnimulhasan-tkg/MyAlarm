# ⏰ MyAlarm — Android Alarm App

A full-featured, production-ready Android alarm application built entirely with **Jetpack Compose** and **Clean Architecture**. Designed with a warm dark UI, smooth animations, and rock-solid alarm scheduling that survives reboots.

---

## 📸 Overview

MyAlarm is a modern Android alarm app that demonstrates real-world architecture patterns and Android system APIs. From exact alarm scheduling to lock-screen UI, every feature is implemented following industry best practices.

---

## 🏛️ Architecture

This project follows **Clean Architecture** with strict layer separation:

```
┌─────────────────────────────────────┐
│         Presentation Layer          │  Jetpack Compose UI + ViewModels
├─────────────────────────────────────┤
│           Domain Layer              │  Use Cases + Repository Interfaces
├─────────────────────────────────────┤
│            Data Layer               │  Room + AlarmManager + Services
└─────────────────────────────────────┘
```

- **Presentation** → Compose screens, `@HiltViewModel`, `StateFlow` UI state, sealed event classes
- **Domain** → Pure Kotlin use cases (`AddAlarmUseCase`, `ToggleAlarmUseCase`, etc.), zero Android imports
- **Data** → Room database, `AlarmManager` scheduling, `BroadcastReceiver`, Foreground `Service`

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| **UI** | Jetpack Compose, Material 3 |
| **Architecture** | Clean Architecture, MVVM |
| **DI** | Hilt (Dagger) |
| **Database** | Room (SQLite) |
| **Async** | Kotlin Coroutines + Flow |
| **Navigation** | Compose Navigation |
| **Scheduling** | Android AlarmManager (`setAlarmClock`) |
| **Background** | Foreground Service, BroadcastReceiver |
| **Build** | Gradle Version Catalog (`libs.versions.toml`), KSP |
| **Language** | Kotlin 100% |

---

## ✨ Features

- ✅ Create, edit, and delete alarms
- ✅ Enable / disable toggle per alarm with animated card state
- ✅ Repeat on selected days (Mon–Sun) with smart labels — *Weekdays*, *Weekends*, *Every day*
- ✅ Custom alarm label
- ✅ Vibration toggle
- ✅ Snooze duration picker (1–30 min)
- ✅ Exact alarm scheduling via `AlarmManager.setAlarmClock()`
- ✅ Alarm fires even on lock screen (`showWhenLocked`, `turnScreenOn`)
- ✅ Full-screen alarm ring UI with pulsing glow animation
- ✅ Snooze and Dismiss from notification shade
- ✅ Alarms persist and reschedule after device reboot (`BootReceiver`)
- ✅ Warm dark theme optimized for nighttime use

---

## 🗂️ Project Structure

```
com.tasnimulhasan.myalarm/
├── data/
│   ├── local/          # Room DB, DAO, Entity, Mapper
│   ├── receiver/       # AlarmReceiver, BootReceiver
│   ├── repository/     # AlarmRepositoryImpl, AlarmSchedulerImpl
│   └── service/        # AlarmService (Foreground)
├── domain/
│   ├── model/          # Alarm domain model
│   ├── repository/     # AlarmRepository, AlarmScheduler interfaces
│   └── usecase/        # Add, Update, Delete, Toggle, Get use cases
├── presentation/
│   ├── alarm/          # Alarm list screen + ViewModel
│   ├── addeditalarm/   # Add/Edit screen + ViewModel + components
│   ├── alarmring/      # Lock screen ring Activity
│   └── ui/theme/       # Color, Type, Theme
└── di/
    └── AppModule.kt    # Hilt dependency bindings
```

---

## 🧩 Architecture Patterns

- **Repository Pattern** — abstracts data sources behind domain interfaces; the domain layer never touches Room or AlarmManager directly
- **Use Case Pattern** — each business operation is an isolated, single-responsibility class, independently testable
- **MVVM** — ViewModels expose immutable `StateFlow<UiState>` and accept sealed `Event` classes from the UI
- **Unidirectional Data Flow (UDF)** — UI sends events → ViewModel processes → emits new state → UI renders
- **Mapper Pattern** — `AlarmEntity` ↔ `Alarm` domain model conversion kept in a dedicated mapper, keeping layers decoupled

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 26+
- JDK 17

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/tasnimulhasan-tkg/MyAlarm.git
   ```

2. Add **Space Grotesk** fonts to `app/src/main/res/font/`
   - Download from [fonts.google.com/specimen/Space+Grotesk](https://fonts.google.com/specimen/Space+Grotesk)
   - Required files:
     - `space_grotesk_regular.ttf`
     - `space_grotesk_medium.ttf`
     - `space_grotesk_semibold.ttf`
     - `space_grotesk_bold.ttf`

3. Build and run
   ```bash
   ./gradlew assembleDebug
   ```

### Permission Note
On **Android 12+**, the app requires the *Alarms & Reminders* exact alarm permission. The system will prompt automatically, or it can be granted via:

> **Settings → Apps → Special App Access → Alarms & Reminders**

---

## 📦 Dependencies

```toml
[versions]
kotlin = "2.3.0"
agp = "9.2.1"
hilt = "2.59.2"
room = "2.8.4"
composeBom = "2026.05.01"
navigationCompose = "2.9.8"
coroutines = "1.11.0"
ksp = "2.3.0-2.0.0"
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

```
MIT License

Copyright (c) 2026 Tasnimul Hasan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

> Built with ❤️ using Jetpack Compose and Clean Architecture principles.
