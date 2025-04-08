# Scriba - A Note Taking App
Welcome to Scriba, a modern note taking application developed in Kotlin for the ECM2425 Mobile and Ubiquitous Computing coursework. Scriba lets users quickly create, edit, delete, and share notes using an intuitive, touch-based interface built with Jetpack Compose.

---
## Table of Contents
  - [Features](#features)
  - [Design & Architecture](#design--architecture)
  - [Setup & Running](#setup--running)
  - [Challenges & Future Improvements](#challenges--future-improvements)

---
## Features
  - **Multiple Screens & Navigation:**  Smooth transitions between the notes list, add/edit note, and settings screens.
  - **Intents & Data Passing:**  Explicit intents manage screen navigation while implicit intents enable sharing of notes.
  - **Responsive, Modern UI:**  Built with Jetpack Compose and Material3, the interface adapts between grid and list views and supports dark mode.
  - **Local Storage:**  Uses Room for persistent note storage and DataStore for managing user preferences.
  - **Real-Time Search & Pinning:**  Instantly filter notes by title or content and pin important notes for quick access.

---
## Design & Architecture
Scriba employs the MVVM pattern for a clear separation of concerns:
  - **ViewModels:** Manage UI state and business logic.
  - **Repository & Room Database:** Handle local data persistence.
  - **DataStore:** Reactively manages user preference settings.

Leveraging Jetpack Compose allows for a modern, declarative UI that is both scalable and easy to maintain.

---
## Setup & Running
1. **Prerequisites:**
   - **Android Studio:** Download and install the latest stable version from [developer.android.com/studio](https://developer.android.com/studio).
   - **Android SDK:** Ensure that you have Android SDK API level 34 installed. This is the default testing environment for the project.
   - **Java Version:** The project is configured to use Java 11.

2. **Cloning the Repository:**
   ```bash
   git clone https://github.com/yourusername/scriba.git
   cd scriba
3. **Opening the Project:**
  - Launch Android Studio.
  - Select File > Open... and choose the project folder you just cloned.
  - Allow Android Studio to index the project and sync Gradle. This will download all necessary dependencies as specified in build.gradle.kts.

4. **Configuring Android Studio:**
   - Ensure your IDE's SDK paths are correctly set via File > Project Structure > SDK Location.
   - Verify that the Gradle build scripts are properly configured by reviewing build.gradle.kts.

5. **Building and Running the App:**
   - **Sync & Rebuild:** Use File > Sync Project with Gradle Files to ensure all dependencies are up-to-date, then perform Build > Clean Project followed by Build > Rebuild Project to compile your code.
   - **Creating an Emulator:** Open Device Manager under Tools. Click the + icon to add a new device, select a device model (e.g., Pixel 7), and choose a system image with API level 34. Follow the prompts to create and launch the emulator.
   - **Running:** Once the emulator is active or an Android device is connected, click the Run button in Android Studio to install and launch Scriba.

---
## Challenges & Future Improvements
**Challenges:**
  - Managing UI state effectively with Jetpack Compose.
  - Ensuring stable local data persistence during configuration changes.
  - Integrating explicit and implicit intents for smooth navigation and sharing without introducing complexity.

**Future Improvements:**
  - Cloud Synchronization: Enable note backup and multi-device access.
  - Voice Input Integration: Add hands-free note creation for greater accessibility.
  - Enhanced Sharing Options: Support multimedia sharing and integration with social platforms.
  - Biometric Security: Implement fingerprint or face recognition to secure sensitive notes.
