# Vitesse

Vitesse is an Android application for managing job candidates. It lets users browse candidate profiles, search and filter favorites, create or edit candidate records, and view detailed information such as contact details, notes, salary expectations, and currency conversion.

## Architecture

The project is organized as a single Android module, `:app`, with a simple layered structure:

- `data` contains the local database, Room DAO, entities, repositories, API responses, and Retrofit client.
- `di` contains the Hilt modules used to provide the database, repositories, and network dependencies.
- `ui` contains the Compose screens, reusable components, navigation setup, theme, and main ViewModel.
- `utils` contains small helper functions and extensions shared across the app.

```text
app/src/main/java/com/example/vitesse
├── data
│   ├── dao            # Room data access
│   ├── db             # Local database
│   ├── entity         # Persistent entities
│   ├── model          # Domain models
│   ├── network        # Retrofit clients
│   ├── repository     # Data repositories
│   └── response       # API responses
├── di                 # Hilt modules
├── ui
│   ├── component      # Reusable Compose components
│   ├── home           # Main ViewModel
│   ├── navigation     # Routes and tabs
│   ├── screen         # Compose screens
│   └── theme          # Compose theme
└── utils              # Extensions and helpers
```

## Installation

1. Clone the repository.
2. Open the project folder in Android Studio.
3. Let Android Studio sync Gradle.
4. Select an emulator or connected Android device.
5. Run the `app` configuration.

## Useful Commands

Build the debug app:

```bash
./gradlew :app:assembleDebug
```

Run unit tests:

```bash
./gradlew :app:testDebugUnitTest
```

Run instrumented tests on a connected device or emulator:

```bash
./gradlew :app:connectedDebugAndroidTest
```

Format Kotlin code:

```bash
./gradlew :app:ktfmtFormat
```

Check Kotlin formatting:

```bash
./gradlew :app:ktfmtCheck
```
