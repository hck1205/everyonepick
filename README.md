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

This starter stays in a single `app` module to keep the first project approachable, but the package boundaries are already separated so you can split them into real Gradle modules later without rewriting the app shape.

- `core/data`: repository contracts and implementations
- `core/data/local`: Room-backed local cache and write coordination
- `core/data/remote`: Retrofit API and remote DTOs
- `core/database`: Room entities, DAO, database
- `core/datastore`: app-level preferences
- `core/designsystem`: theme and design tokens
- `core/di`: Hilt modules
- `core/model`: domain models
- `core/navigation`: routes and `NavHost`
- `core/ui`: app root and global app state
- `feature/main`: app entry feature that can later evolve into a feed or dashboard
- `feature/settings`: sample feature with preference-driven theming
- `feature/vote`: the current primary domain feature

## What Is Included

- A vote-first main screen backed by Room
- A vote data layer split into remote/local/repository responsibilities
- A dedicated vote domain feature that can later move behind a richer main screen
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
