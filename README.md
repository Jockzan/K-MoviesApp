# MoviesApp

MoviesApp is a movie discovery app that fetches movie details, genres, and videos using The Movie Database (TMDb) API. It supports features like real-time search, pagination, dark/light mode, and error handling.

## Project Details

### Architecture
The app follows a clean architecture pattern, dividing responsibilities into different layers:

- **Data Layer**: Handles network calls and maps data to domain models. This includes classes like:
    - `MoviesRepositoryImpl`
    - `GenreRepositoryImpl`
    - `DetailsRepositoryImpl`
    - Network classes like `NetworkInteractorImpl`

- **Domain Layer**: Contains models and repository interfaces used by the app.

- **Presentation Layer**: Uses Jetpack Compose to render UI and ViewModels to handle app state. Examples include:
    - `MoviesViewModel`: Manages movie data for different categories and real-time search.
    - `MovieDetailsViewModel`: Handles fetching details and videos for a movie.

### Features
- **Dynamic Locale Support**: Automatically adjusts language (English/Spanish) based on device settings.
- **Real-time Search**: Implements debounced filtering for search results.
- **Error Handling**: Displays appropriate messages for network issues or API errors.
- **Dark/Light Mode**: Adapts UI colors dynamically.
- **Pagination**: Fetches additional movie data as users scroll.

### Libraries Used

- **Jetpack Compose**: For building the UI declaratively.
- **Hilt**: Dependency Injection.
- **Retrofit**: For API calls.
- **Moshi**: JSON parsing.
- **Coroutines**: For asynchronous programming.
- **Coil**: Image loading.

### Classes with Detailed Comments
The following classes have detailed comments to explain their logic:
- `MoviesViewModel`
- `MovieDetailsViewModel`
- `NetworkInteractorImpl`
- `MoviesRepositoryImpl`
- `GenreRepositoryImpl`
- `DetailsRepositoryImpl`
- `DateUtils`
- `LocalUtils`

## How to Run the Project

### Prerequisites
- Android Studio.
- A valid API key from [The Movie Database (TMDb)](https://www.themoviedb.org/documentation/api).

### Setup Instructions
1. Clone the repository:
   ```bash
   git clone git@github.com:Jockzan/K-MoviesApp.git
   ```

2. Create a file named `api.properties` in the root of the project with the following content:
   ```
   API_TOKEN=<your_api_key>
   ```

3. Add the `API_TOKEN` value to your repository secrets for workflows (if needed).

4. Open the project in Android Studio.

5. Build and run the app on a physical or virtual device.

## How to Test
- Search for movies in real-time using the search bar.
- Scroll through categories like Popular, Top Rated, Upcoming, and Now Playing.
- Switch device language between English and Spanish to see localization in action.
- Enable dark mode on the device to test UI adaptation.

---
