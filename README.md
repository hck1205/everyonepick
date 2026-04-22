# EveryonePick Android Starter

Structured Android starter for a first native app, optimized for an experienced developer who wants a clean baseline instead of a toy template.

## Stack

- Kotlin
- Jetpack Compose
- Hilt
- Navigation Compose
- Room
- DataStore
- Version Catalog + Kotlin DSL

## Structure

This starter now uses a small multi-module layout so the app entry layer, vote domain, and reusable core pieces can evolve independently.

- `app`: application shell, navigation, and app-wide state
- `core/common`: shared DI qualifiers and cross-cutting utilities
- `core/data`: repository contracts plus remote/local implementations
- `core:data/network`: shared network stack used by feature-specific APIs
- `core/database`: Room entities, DAO, database, and database DI
- `core/designsystem`: theme and design tokens
- `core/model`: domain models
- `core:preferences`: app preference storage and theme preference models
- `feature/main`: app entry feature that can later evolve into a feed or dashboard
- `feature/settings`: dedicated settings feature module
- `feature/vote`: the current primary domain feature

## What Is Included

- A vote-first main screen backed by Room
- A vote data layer split into remote/local/repository responsibilities
- A reusable shared network layer inside `core:data`
- A dedicated vote domain feature that can later move behind a richer main screen
- A dedicated settings feature module
- A settings screen backed by DataStore
- Theme mode switching: system, light, dark
- DI wiring with Hilt
- Retrofit and OkHttp wiring with a fake in-app API interceptor until a real backend exists
- Navigation wiring
- A small unit test

## Requirements

- Android Studio recent stable
- JDK 17 for CLI builds
- Android SDK with API 36 installed

## First Run

1. Open this folder in Android Studio.
2. Let Android Studio sync Gradle and install any missing SDK packages.
3. Run the `app` configuration on an emulator or device.

## Notes

- The repository includes Gradle wrapper scripts and properties. If Gradle distribution files are not already cached locally, the first sync will download them.
- The machine that generated this project had `JAVA_HOME` still pointing to JDK 8. If CLI builds fail, switch to JDK 17 or use Android Studio's bundled JBR.
