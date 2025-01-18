# WatchIt - Movie Discovery & Tracking App

## Overview
WatchIt is a modern Android application built with Kotlin that allows users to discover, track, and manage their favorite movies. The app integrates with The Movie Database (TMDb) API to provide users with comprehensive movie information, watchlist functionality, and personalized movie recommendations.

## Features
- **Movie Discovery**: Browse popular, top-rated, and upcoming movies
- **Detailed Movie Information**: View comprehensive details including ratings, genres, and overviews
- **Watchlist Management**: Add/remove movies to personal watchlist
- **User Authentication**: Secure login with TMDb account

## Technical Stack
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Dagger Hilt
- **Networking**: Retrofit with KotlinX Serialization
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Image Loading**: Glide
- **Navigation**: Jetpack Navigation Component
- **UI Components**: Material Design Components

## Project Structure
- app/
- ├── data/
- │ ├── model/ # Data models and entities
- │ ├── network/ # API interfaces and network configuration
- │ └── repository/ # Repository implementations
- ├── di/ # Dependency injection modules
- ├── domain/
- │ └── repository/ # Repository interfaces
- ├── ui/
- │ ├── movieDetail/ # Movie details screen
- │ ├── movieList/ # Movie listing screen
- │ ├── profile/ # User profile screen
- │ └── watchlist/ # Watchlist management
- └── common/ # Shared utilities and constants


## Architecture
The application follows Clean Architecture principles with MVVM pattern:
- **Presentation Layer**: Activities/Fragments with ViewModels
- **Domain Layer**: Use cases and repository interfaces
- **Data Layer**: Repository implementations and data sources

## Key Components

### ViewModels
- `MovieDetailViewModel`: Handles movie details and watchlist operations
- `WatchlistViewModel`: Manages user's watchlist
- `MainViewModel`: Handles main screen movie listings

### Repositories
- `AppRepository`: Interface defining core application operations
- `AppRepositoryImpl`: Implementation handling data operations and API calls

### UI State Management
The app uses a sealed class `UIState` for handling different states

## App Design
<!-- First Row -->
<p align="center">
  <img src="https://github.com/EbrarTikit/Hilt-Android-WatchIt_App/blob/master/Screenshots/home.png" alt="Image 1" width="30%">
  <img src="https://github.com/EbrarTikit/Hilt-Android-WatchIt_App/blob/master/Screenshots/detail.png" alt="Image 2" width="30%">
  <img src="https://github.com/EbrarTikit/Hilt-Android-WatchIt_App/blob/master/Screenshots/Log%20in.png" alt="Image 3" width="30%">
</p>

<!-- Second Row -->
<p align="center">
  <img src="https://github.com/EbrarTikit/Hilt-Android-WatchIt_App/blob/master/Screenshots/trends.png" alt="Image 4" width="30%">
  <img src="https://github.com/EbrarTikit/Hilt-Android-WatchIt_App/blob/master/Screenshots/watchlist.png" alt="Image 5" width="30%">
</p>


## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or later
- Android SDK with minimum API level 33

### Configuration
1. Clone the repository
2. Add your TMDb API key in `common/Const.kt`
3. Build and run the project

## Authentication
The app uses TMDb (The Movie Database) API for authentication. To use the app:

1. Create a TMDb account at [themoviedb.org](https://www.themoviedb.org/signup)
2. Log in to the app using your TMDb credentials
3. After successful authentication, you can:
   - Add movies to your watchlist
   - View your profile information
   - Access your movie ratings
   - Manage your watchlist

### Authentication Flow
1. The app requests a token from TMDb API
2. User enters their TMDb username and password
3. The app validates the token with provided credentials
4. Upon successful validation, a session is created
5. Session ID is stored securely for subsequent requests

### Implementation Details
The authentication is implemented using:
- Secure token-based authentication
- Session management
- Encrypted credential transmission
- Persistent session storage

## Acknowledgments
- [The Movie Database (TMDb)](https://www.themoviedb.org/) for providing the movie data API
- Material Design Components for Android
- All other open-source libraries used in this project
