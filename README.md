# NewsJetPackApp

A modern Android News application built with Jetpack Compose, following Clean Architecture and MVI (Model-View-Intent) principles. This project showcases the latest Android Jetpack libraries and modern development practices.

## 🚀 Features

- **Top Headlines**: Stay updated with the latest news from various countries and categories.
- **Search**: Robust article search functionality using keywords.
- **Favorites**: Locally bookmark your favorite articles to read offline.
- **Article Details**: Detailed view for in-depth reading of news articles.
- **Profile**: Manage user settings and application preferences.
- **Dynamic Theming**: Responsive UI that adapts to user preferences.
- **Offline Support**: Robust local caching of news data.

## 🛠 Tech Stack & Libraries

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (BOM 2025.11.00)
- **Navigation**: [Navigation 3](https://developer.android.com/guide/navigation/navigation-kotlin-dsl) (Experimental)
    - `androidx.navigation3:navigation3-ui`
    - `androidx.navigation3:navigation3-runtime`
    - `androidx.compose.material3.adaptive:adaptive-navigation3`
- **Architecture**:
    - **Clean Architecture**: Separation into Data, Domain, and Presentation layers.
    - **MVI**: Unidirectional data flow for robust state management.
- **Dependency Injection**: [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) with Hilt Navigation Compose.
- **Networking**: [Ktor 3.2.0](https://ktor.io/)
    - `ktor-client-resources`
    - `ktor-client-logging`
    - `ktor-serialization-kotlinx-json`
- **Local Storage**:
    - [Room 2.8.3](https://developer.android.com/training/data-storage/room) for structured database persistence.
    - [DataStore Preferences 1.1.7](https://developer.android.com/topic/libraries/architecture/datastore) for lightweight key-value storage.
- **Image Loading**: [Coil 2.7.0](https://coil-kt.github.io/coil/compose/)
- **Concurrency**: Kotlin Coroutines & Flow.
- **Lifecycle**: [Lifecycle Runtime 2.9.4](https://developer.android.com/jetpack/androidx/releases/lifecycle) with `lifecycle-viewmodel-navigation3`.

## 📁 Project Structure

The project follows a modularized package structure within the `newsapp` module:

```text
be.business.newsapp/
├── core/                # Core framework and cross-cutting concerns
│   ├── data/            # Data sources (Remote/Local) and Repository implementations
│   ├── di/              # Hilt modules for Dependency Injection
│   └── presentation/    # Base MVI classes (BaseViewModel, State, Event, Action)
├── domain/              # Pure business logic (Platform independent)
│   ├── model/           # Domain models and entities
│   ├── repository/      # Repository interface definitions
│   └── usecase/         # Discrete business logic operations
├── feature/             # Feature-specific UI and ViewModels
│   ├── articledetail/   # News article detailed view
│   ├── favorite/        # Saved/Bookmarked news
│   ├── home/            # Main news dashboard
│   ├── profile/         # User settings and preferences
│   └── search/          # Search functionality
├── navigation/          # Navigation logic and route definitions
├── ui/                  # Shared UI components and Theme
│   ├── components/      # Reusable UI widgets (CustomNavBar, etc.)
│   └── theme/           # Color palettes, Typography, and Shapes
└── utils/               # Extension functions and common utilities
```

## 🏗 Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/NewsJetPackApp.git
   ```
2. **API Configuration**:
   The app uses [NewsAPI.org](https://newsapi.org/). Obtain an API key and place it in the project (news.properties file).
3. **Build the Project**:
   Open the project in **Android Studio Ladybug** (or newer) and sync Gradle.
4. **Run**:
   Select the `newsapp` configuration and run on an emulator or physical device.

## 🧪 Testing

The project includes unit tests for business logic and ViewModels.
- Run tests: `./gradlew test`

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
