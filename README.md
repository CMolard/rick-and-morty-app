# Context

This is a mobile application designed to evaluate my skills in Kotlin MP development.

## Project Structure

This project is a Kotlin Multiplatform project targeting both Android and Desktop (JVM) platforms.

### Code Architecture

The code is organized into several layers, each with a specific responsibility. The architecture follows the Clean Architecture and Model-View-Intent (MVI) pattern, ensuring a unidirectional data flow and clear separation of concerns.

#### Clean Architecture

Clean Architecture divides the project into three main layers: UI, Domain, and Data.

1. **UI Layer**: This layer is responsible for the presentation logic and user interface. It includes Compose Multiplatform code for shared UI components and platform-specific UI code for Android and Desktop.
2. **Domain Layer**: This layer contains the business logic. It includes use cases and interfaces for the repositories.
3. **Data Layer**: This layer handles data management. It includes implementations of the repositories, data sources, and network clients.

##### Communication Flow

- The **UI Layer** interacts with the **Domain Layer** through use cases or repositories interfaces.
- The **Domain Layer** defines the business logic and interacts with the **Data Layer** through repository interfaces.
- The **Data Layer** provides data to the **Domain Layer** by implementing the repository interfaces.

##### Clean Architecture Diagram

```plaintext
+----------------+       +----------------+       +----------------+
|      UI        | ----> |     Domain     | ----> |      Data      |
| (Presentation) |       |(Business Logic)|       | (Data Handling)|
+----------------+       +----------------+       +----------------+
```

##### Example

The `LocationDetailsScreen` in the UI layer interacts with the `LocationDetailsViewModel` to handle UI logic and interaction, which in turn interacts with the `LocationRepository` in the Domain layer to retrieve data for example. With dependency injection, it will use the `LocationRepositoryImpl` in the Data layer. This implementation will use the `LocationApi` to retrieve data from the API and store Location data in local storage by using the `LocationDao`.

#### Model-View-Intent (MVI) Architecture

MVI ensures a unidirectional data flow and clear separation of concerns by dividing the application into three main components: Model, View, and Intent.

1. **Model**: This component contains the state and business logic. It includes view model to communicate with repositories, use cases, and data sources.
2. **View**: This component is responsible for the UI. It includes Compose Multiplatform code for shared UI components and platform-specific UI code for Android and Desktop platforms.
3. **Intent**: This component handles user actions and intents, transforming them into actions that update the Model.

##### Communication Flow

- The **View** sends user intents to the **Intent** component.
- The **Intent** component processes these intents and interacts with the **Model** component.
- The **Model** component updates the state based on the actions and notifies the **View** component of any changes.

##### MVI Architecture Diagram

```plaintext
+----------------+       +----------------+       +----------------+
|     View       | ----> |     Intent     | ----> |     Model      |
| (UI Components)|       | (User Actions) |       | (Business Logic)|
+----------------+       +----------------+       +----------------+
       ^                                              |
       |----------------------------------------------|
```

##### Example

The `LocationDetailsScreen` sends user intents to the `LocationDetailsViewModel`, which processes these intents and interacts with the `LocationRepository` to update the state and notify the `LocationDetailsScreen` of any changes.

### Folder Structure

- composeApp: Contains shared code for Compose Multiplatform applications.
  - `commonMain`: Code common to all targets.
  - `androidMain`: Android-specific code.
  - `desktopMain`: Code specific to the Desktop platform (JVM).

### Getting Started

To build and run the project, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/CMolard/rick-and-morty-app.git
   ```
2. Checkout the `features/LocationPreview` branch:
   ```sh
   git checkout features/LocationPreview
   ```
3. Navigate to the project directory:
   ```sh
   cd rick-and-morty-app
   ```
4. Build the project:
   ```sh
   ./gradlew build
   ```
5. Run the application:
   ```sh
   ./gradlew run
   ```

### Dependencies

The project uses the following dependencies:

- Kotlin Multiplatform
- Compose Multiplatform
- Koin for dependency injection
- Ktor for network requests
- Room for local storage