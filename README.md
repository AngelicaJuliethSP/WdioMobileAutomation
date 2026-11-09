# Framework de Automatización WdioMobile

Framework de automatización de pruebas para la **WebdriverIO Native Demo App** (Android) construido con Appium (UiAutomator2), Java y TestNG, siguiendo el patrón **Screen Object Model** (una clase por pantalla, con localizadores y acciones encapsulados).

## Descripción general

Este proyecto automatiza cuatro flujos de usuario principales de la aplicación nativa demo de WebdriverIO:

- **Login**: inicio de sesión con credenciales válidas y verificación del diálogo de confirmación "You are logged in!".
- **Signup**: creación de una nueva cuenta con datos autogenerados y verificación del diálogo de confirmación.
- **Swipe**: deslizamiento horizontal de tarjetas hasta llegar al texto oculto (`You found me!!!`) de la última tarjeta.
- **Navigation**: navegación por las pestañas del menú inferior (Home, Web, Forms, Swipe, Drag, Menu).

## Stack tecnológico

| Herramienta | Versión | Propósito |
|---|---|---|
| Java | 17 | Lenguaje de programación |
| Maven | 3.x | Compilación y gestión de dependencias |
| Appium Java Client | 10.1.1 | Automatización móvil |
| Appium Server | 3.9.0 | Servidor de automatización |
| Driver UiAutomator2 | 8.7.3 | Motor de automatización Android |
| TestNG | 7.10.2 | Ejecutor de pruebas y anotaciones |
| Emulador Android | API 30 (CI) / API 34+ (local) | Dispositivo objetivo (AVD Pixel 6) |

## Estructura del proyecto

```
WdioMobileAutomation/
├── apps/                            # APK objetivo (no versionado)
├── src/
│   └── test/java/
│       ├── base/
│       │   └── BaseTest.java        # Ciclo de vida del driver (@BeforeMethod/@AfterMethod)
│       ├── drivers/
│       │   └── DriverManager.java   # Proveedor de AndroidDriver (config externalizada)
│       ├── screens/
│       │   ├── BaseScreen.java      # Esperas y helpers compartidos
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
│           └── DataGenerator.java   # Generación de datos de prueba aleatorios
├── pom.xml
└── .gitignore
```

## Decisiones de diseño

- **Screen Object Model**: cada pantalla de la app es una clase que expone acciones de alto nivel; los localizadores viven exclusivamente en las clases de pantallas y las pruebas nunca tocan `WebElement`/`driver` directamente.
- **Configuración externalizada**: la ruta del APK, el nombre del dispositivo y la URL de Appium se resuelven con la prioridad `propiedad del sistema -D` → `variable de entorno` → `valor por defecto`, de modo que la suite corre en cualquier máquina sin cambios de código.
- **Validación del APK**: `DriverManager` resuelve rutas relativas del APK a absolutas (Appium resuelve rutas relativas contra su propio directorio de trabajo) y falla rápido con un mensaje accionable si el archivo no existe.
- **Esperas explícitas**: los helpers de espera compartidos en `BaseScreen` evitan esperas fijas y búsquedas de elementos inestables.

### Limitaciones conocidas

- **Driver no thread-safe**: `DriverManager` mantiene el `AndroidDriver` en un campo `static` (una sesión por JVM). Es suficiente para la ejecución secuencial de TestNG, pero **no permite ejecución en paralelo**; para paralelismo habría que migrar a `ThreadLocal<AndroidDriver>`.
- **Gestos de swipe**: `swipeElementHorizontally` usa un margen del 5 % en los bordes para no terminar en x=0 (donde Android puede interpretar el gesto como "atrás"). `swipeForwardAndWait` falla explícitamente si la tarjeta sigue visible tras 3 intentos. Aun así, el renderizado por software del emulador (SwiftShader) puede hacer este test intermitente.
- El escenario **Navigation** requiere que el WebView de la app renderice; en emuladores headless con renderizado por software (SwiftShader) el contenido del WebView puede no cargar, causando el fallo del escenario. En emuladores con aceleración por hardware pasa consistentemente.
- El escenario **Swipe** es intermitente en emuladores headless con renderizado por software: los gestos de deslizamiento no siempre son procesados por el sistema, por lo que el escenario puede fallar aunque el framework y los localizadores sean correctos. En emuladores con aceleración por hardware pasa consistentemente (verificado durante la fase de refactorización).

Ambos escenarios (Navigation y Swipe) se ejecutan en CI en un paso separado con `maven.test.failure.ignore=true`, de modo que sus fallos quedan registrados en los reportes de Surefire/Allure como **fallas conocidas** (evidencia documentada) sin romper el pipeline.

## Configuración

| Parámetro | Propiedad del sistema | Variable de entorno | Por defecto |
|---|---|---|---|
| URL del servidor Appium | `-Dappium.url` | `APPIUM_URL` | `http://127.0.0.1:4723` |
| Nombre del dispositivo | `-Ddevice.name` | `DEVICE_NAME` | `Pixel_6` |
| Ruta del APK | `-Dapp.path` | `APP_PATH` | `apps/android.wdio.native.app.v2.2.0.apk` |

## Cómo ejecutar

### Prerrequisitos

- JDK 17+
- Maven 3.6+
- Node.js + Appium Server 3.9+ con el driver `uiautomator2` (8.7.3+)
- Android SDK con un emulador (se recomienda AVD `Pixel_6`) corriendo API 34+
- El APK ubicado en `apps/android.wdio.native.app.v2.2.0.apk` (o configurado vía `APP_PATH`)

### Ejecución

```bash
# iniciar el emulador, luego:
appium --address 127.0.0.1 --port 4723

# en otra terminal:
mvn clean test
```

Con configuración personalizada:

```bash
mvn clean test -Dappium.url=http://127.0.0.1:4723 -Ddevice.name=Pixel_6 -Dapp.path=/ruta/a/app.apk
```

## Pipeline CI/CD

La suite corre automáticamente en GitHub Actions en cada push/PR a `main`,
manualmente (workflow_dispatch) y **a diario a las 06:00 UTC (01:00 Colombia)**
vía cron, sobre un emulador Android (API 30, Pixel 6). Cada ejecución produce:

| Artefacto | Descripción |
|---|---|
| `surefire-reports-mobile` | Reportes XML de Surefire |
| `allure-results-mobile` | Resultados crudos de Allure |
| `allure-report-mobile` | Reporte HTML navegable de Allure |
| `appium-log` | Log del servidor Appium |

### Publicación automática al dashboard

Después de cada ejecución (incluso con fallos), el pipeline parsea los XML de
Surefire y publica los resultados consolidados en
[Proyecto-grado-dashboard](https://github.com/AngelicaJuliethSP/Proyecto-grado-dashboard),
que alimenta el dashboard público de QA:
**https://proyecto-grado-dashboard.streamlit.app**

Esto hace que el dashboard se actualice de forma **100 % automática** — sin
pasos manuales. Requiere el secreto `DASHBOARD_TOKEN` (PAT fine-grained con
acceso de escritura al repo del dashboard). Si el secreto no existe, el paso
se omite de forma controlada y la ejecución no se ve afectada.

## Autora

Angélica Julieth Silva Pérez — College Trainee, Globant
