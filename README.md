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

## Author

Angélica Julieth Silva Pérez — College Trainee, Globant
