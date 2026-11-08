# WdioMobile Automation Framework

Test automation framework for the **WebdriverIO Native Demo App** (Android) built with Appium (UiAutomator2), Java, and TestNG, following the Screenplay-like **Screen Object Model** pattern.

## Overview

This project automates four core user flows on the WebdriverIO native demo application:

- **Login**: valid credentials login and successful logout verification.
- **Signup**: new account creation with auto-generated data and verification of the confirmation dialog.
- **Swipe**: horizontal card swiping until reaching the hidden text (`FIND ME` 🎉) on the last card.
- **Navigation**: navigation through all bottom menu tabs (Home, Web, Login, Forms, Swipe, Drag).

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Maven | 3.x | Build and dependency management |
| Appium Java Client | 9.x | Mobile automation |
| Appium Server | 3.9.0 | Automation server |
| UiAutomator2 Driver | 8.7.3 | Android automation engine |
| TestNG | 7.10.2 | Test runner and annotations |
| Android Emulator | API 37.1 | Target device (Pixel 6 AVD) |

## Project Structure

```
WdioMobileAutomation/
├── apps/                            # Target APK (not versioned)
├── src/
│   └── test/java/
│       ├── base/
│       │   └── BaseTest.java        # Driver lifecycle (@BeforeMethod/@AfterMethod)
│       ├── drivers/
│       │   └── DriverManager.java   # AndroidDriver provider (externalized config)
│       ├── screens/
│       │   ├── BaseScreen.java      # Shared waits and helpers
│       │   ├── HomeScreen.java
│       │   ├── LoginScreen.java
│       │   ├── MenuScreen.java
│       │   ├── FormsScreen.java
│       │   ├── SwipeScreen.java
│       │   ├── DragScreen.java
│       │   └── WebScreen.java
│       ├── tests/
│       │   ├── LoginTest.java
│       │   ├── SignupTest.java
│       │   ├── SwipeTest.java
│       │   └── NavigationTest.java
│       └── utils/
│           └── DataGenerator.java   # Random test data generation
├── pom.xml
└── .gitignore
```

## Design Decisions

- **Screen Object Model**: each app screen is a class exposing high-level actions; locators live exclusively in screen classes and tests never touch `WebElement`/`driver` directly.
- **Externalized configuration**: APK path, device name, and Appium URL are resolved with the priority `-Dsystem property` → `environment variable` → `default value`, so the suite runs on any machine without code changes.
- **APK validation**: `DriverManager` resolves relative APK paths to absolute (Appium resolves relative paths against its own working directory) and fails fast with an actionable message if the file is missing.
- **Explicit waits**: shared wait helpers in `BaseScreen` avoid fixed sleeps and flaky element lookups.

## Configuration

| Parameter | System property | Environment variable | Default |
|---|---|---|---|
| Appium server URL | `-Dappium.url` | `APPIUM_URL` | `http://127.0.0.1:4723` |
| Device name | `-Ddevice.name` | `DEVICE_NAME` | `Pixel_6` |
| APK path | `-Dapp.path` | `APP_PATH` | `apps/android.wdio.native.app.v2.2.0.apk` |

## How to Run

### Prerequisites

- JDK 17+
- Maven 3.6+
- Node.js + Appium Server 3.9+ with the `uiautomator2` driver (8.7.3+)
- Android SDK with an emulator (AVD `Pixel_6` recommended) running API 34+
- The APK placed at `apps/android.wdio.native.app.v2.2.0.apk` (or configured via `APP_PATH`)

### Execute

```bash
# start the emulator, then:
appium --address 127.0.0.1 --port 4723

# in another terminal:
mvn clean test
```

With custom configuration:

```bash
mvn clean test -Dappium.url=http://127.0.0.1:4723 -Ddevice.name=Pixel_6 -Dapp.path=/path/to/app.apk
```

## Known Limitations

- The **Navigation** scenario requires the app's WebView to render; on headless emulators with software rendering (SwiftShader) the WebView content may not load, causing the scenario to fail. On emulators with hardware acceleration it passes consistently.
- The **Swipe** scenario is intermittent on headless emulators with software rendering: swipe gestures are not always processed by the system, so the scenario may fail even though the framework and locators are correct. On emulators with hardware acceleration it passes consistently (verified during the refactor phase).

Both scenarios are executed in CI in a separate step with `maven.test.failure.ignore=true`, so their failures are recorded in the Surefire/Allure reports as **known failures** (documented evidence) without breaking the pipeline.

## CI/CD Pipeline

The suite runs automatically on GitHub Actions on every push/PR to `main`,
manually (workflow_dispatch), and **daily at 06:00 UTC (01:00 Colombia)** via
cron, on an Android emulator (API 30, Pixel 6). Each run produces:

| Artifact | Description |
|---|---|
| `surefire-reports-mobile` | Surefire XML reports |
| `allure-results-mobile` | Allure raw results |
| `allure-report-mobile` | Navigable Allure HTML report |
| `appium-log` | Appium server log |

### Automatic Dashboard Publishing

After every run (even on failure), the pipeline parses the Surefire XML and
pushes the consolidated results to
[Proyecto-grado-dashboard](https://github.com/AngelicaJuliethSP/Proyecto-grado-dashboard),
which feeds the public QA dashboard:
**https://proyecto-grado-dashboard.streamlit.app**

This makes the dashboard update **fully automatically** — no manual steps.
It requires the `DASHBOARD_TOKEN` secret (fine-grained PAT with write access
to the dashboard repo). If the secret is missing, the step is skipped
gracefully and the run is not affected.

## Author

Angélica Julieth Silva Pérez — College Trainee, Globant
