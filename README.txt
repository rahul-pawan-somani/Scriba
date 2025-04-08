Scriba - A Note Taking App

Introduction
-------------
Scriba is an Android note taking app designed for the ECM2425 Mobile and Ubiquitous Computing coursework. Developed entirely by me, this application lets users create, edit, delete, and share notes with ease. The app features multiple screens, intuitive navigation, and a modern, touch-based UI built with Jetpack Compose. Core functionalities include:
- Multiple Screens & Navigation: Seamless transitions between the notes list, add note, edit note, and settings screens.
- Intents: Explicit intents manage screen transitions, while implicit intents enable note sharing.
- Responsive UI: Options menus, context menus, and RecyclerView-like displays (using Compose’s LazyColumn/LazyGrid) enhance interactivity.
- Data Storage: Notes are stored in a Room database, and user preferences (e.g., dark mode, view mode) are managed with DataStore.

Design Rationale
-----------------
The app follows the MVVM architecture to cleanly separate UI, business logic, and data operations:
- MVVM: ViewModels handle presentation logic, interfacing with a Repository that communicates with the Room database.
- Jetpack Compose & Material3: These technologies provide a modern, declarative UI that adapts smoothly to various screen sizes and device orientations.
- DataStore for Preferences: Ensures that user settings are stored reliably and observed reactively.

Novel Features
---------------
- Pinned Notes: Important notes can be pinned to appear at the top for quick access.
- Real-Time Search: Users can filter notes instantly by title or content.
- Theme Customization: Dark mode can be toggled to enhance usability in different lighting conditions.
- Adaptive Layout: Supports both grid and list views based on user preference.

Challenges & Future Improvements
----------------------------------
Challenges:  
Managing the UI state with Compose and ensuring stable data persistence during configuration changes were key challenges.

Future Improvements:  
- Cloud Sync: Enable backup and multi-device access.
- Voice Input: Add hands-free note creation.
- Enhanced Sharing: Support multimedia sharing and social media integrations.
- Biometric Security: Enhance data protection for sensitive notes.

Setup & Running
---------------
1. Requirements:
– Android Studio (latest version recommended)  
– Android SDK API level 34 (default testing environment)

2. Setup:
– Unzip the project folder and open it in Android Studio.  
– Allow Gradle to sync and download all dependencies.  
– No further configuration beyond the standard Android SDK setup is required.

3. Running:
– Connect an Android device or run an emulator with API level 34 or higher.  
– Build and run the project from Android Studio.
