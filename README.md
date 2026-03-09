<div align="center">

# 🗞️ Jetpack News App

### Production-Ready Android News App

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack_Compose-Material3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Clean_+_MVI-0A7EA4?style=for-the-badge)
![DI](https://img.shields.io/badge/DI-Hilt-34A853?style=for-the-badge)
![Database](https://img.shields.io/badge/Database-SQLDelight-FF6F00?style=for-the-badge)
![Storage](https://img.shields.io/badge/Session-DataStore-0097A7?style=for-the-badge)
![MinSDK](https://img.shields.io/badge/Android-MinSDK_24-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Build](https://img.shields.io/badge/Build-Passing-44CC11?style=for-the-badge)

A modern news app with public headlines, local auth, per-user favorites, profile management, and feature-first Clean Architecture.

</div>

---

## ✨ Highlights

- Public **Home** feed (works without login)
- Local **Auth** with **Login + Register**
- **Favorites** protected by login and persisted per user
- **Favorites unauthenticated state** shows inline login prompt
- **Profile** with modern elevated UI and account metrics
- **Article Detail** in-app reader with top-bar back navigation
- Material3 Compose UI with loading/error/empty/skeleton states
- Global gradient background effect across screens

## 🧱 Tech Stack

- Kotlin + Coroutines + Flow/StateFlow
- Jetpack Compose + Material3
- Navigation 3
- Hilt
- SQLDelight
- DataStore
- Ktor
- Coil

## 🏛 Full Architecture Diagram

```mermaid
flowchart LR
    UI["UI Layer\nCompose Screens\nHome, Auth, Favorites, Profile, Detail"] --> MVI["Presentation Layer\nState · Action · Event · Reducer · ViewModel"]
    MVI --> USE["Domain Use Cases\nGetTopNews\nLogin/Register/Logout\nObserve/Toggle Favorites\nGetProfile Data"]

    USE --> REPO_NEWS["NewsRepository"]
    USE --> REPO_AUTH["AuthRepository"]
    USE --> REPO_FAV["FavoritesRepository"]

    REPO_NEWS --> API["Remote Data Source\nKtor News API"]
    REPO_AUTH --> DB["SQLDelight Database\nUser + Favorites tables"]
    REPO_FAV --> DB

    REPO_AUTH --> SESSION["DataStore Session\ncurrentUserId"]
    REPO_FAV --> SESSION

    DI["Hilt Dependency Graph"] --> UI
    DI --> MVI
    DI --> USE
    DI --> REPO_NEWS
    DI --> REPO_AUTH
    DI --> REPO_FAV
    DI --> API
    DI --> DB
    DI --> SESSION
```

## 🗂 Complete Code Structure

```text
app/newsapp/src/main/java/be/business/newsapp/
├── MainActivity.kt
├── MainContract.kt
├── MainViewModel.kt
├── NewsApp.kt
├── core/
│   ├── common/
│   │   └── UiState.kt
│   ├── data/
│   │   ├── local/
│   │   │   ├── datastore/
│   │   │   │   ├── DataManager.kt
│   │   │   │   └── PreferenceRepositoryImpl.kt
│   │   │   ├── session/
│   │   │   │   └── UserSessionStore.kt
│   │   │   └── sqldelight/
│   │   │       └── SqlDelightModule.kt
│   │   ├── remote/
│   │   │   ├── apiimpl/
│   │   │   ├── apis/
│   │   │   └── network/
│   │   └── repository/
│   │       ├── NewsRepositoryImpl.kt
│   │       └── PreferencesRepository.kt
│   ├── di/
│   │   ├── CoilModule.kt
│   │   ├── DataStoreModule.kt
│   │   ├── DatabaseModule.kt
│   │   ├── NetworkModule.kt
│   │   ├── RepositoryModule.kt
│   │   └── UseCaseModule.kt
│   ├── domain/
│   │   └── model/
│   │       └── Article.kt
│   └── presentation/
│       ├── AppState.kt
│       ├── BaseStateViewModel.kt
│       ├── BaseViewModel.kt
│       ├── ComposeMVIExtensions.kt
│       └── MviContract.kt
├── domain/
│   ├── model/
│   │   ├── genericresponse/
│   │   ├── newsresponse/
│   │   └── NewsResponse.kt
│   ├── repository/
│   │   └── NewsRepository.kt
│   └── usecase/
│       └── news/
├── feature/
│   ├── articledetail/
│   │   └── ArticleDetailScreen.kt
│   ├── auth/
│   │   ├── data/
│   │   ├── domain/
│   │   ├── navigation/
│   │   ├── presentation/
│   │   └── ui/
│   ├── favorites/
│   │   ├── data/
│   │   ├── domain/
│   │   ├── navigation/
│   │   ├── presentation/
│   │   └── ui/
│   ├── home/
│   │   ├── components/
│   │   ├── domain/
│   │   ├── navigation/
│   │   └── presentation/
│   ├── profile/
│   │   ├── navigation/
│   │   └── presentation/
│   └── search/
│       ├── SearchScreen.kt
│       └── navigation/
├── navigation/
│   ├── Entries.kt
│   ├── Navigator.kt
│   ├── NavRoutes.kt
│   └── NewsAppNavDisplay.kt
├── ui/
│   ├── components/
│   ├── shared/
│   └── theme/
└── utils/
```

SQL schema:

- `app/newsapp/src/main/sqldelight/be/business/newsapp/core/data/local/sqldelight/NewsSqlDatabase.sq`

## 📸 Screenshots

> Add images to `docs/screenshots/` with these names: `home.png`, `login.png`, `favorites.png`, `profile.png`, `detail.png`

| Home | Login |
|------|-------|
| ![Home](docs/screenshots/home.png) | ![Login](docs/screenshots/login.png) |

|  Detail    | Profile |
|-----------|---------|
| ![Detail](docs/screenshots/article_detail.png) | ![Profile](docs/screenshots/profile.png) |


## 🚀 Build

```bash
./gradlew :app:assembleDevDebug
```

## ✅ Tests

```bash
./gradlew :app:testDevDebugUnitTest
```

Included tests:

- Auth reducer tests
- Home reducer tests
- Favorites use-case tests
