# Release Notes for v0.2.0
## Release Date: 12 Aug 2024

## Overview
This v0.2.0 release marks a significant step forward in the development of my project, applying several key features and setting the foundation for future development. This update includes the initial implementation of user authentication, task management, and the integration of Firebase for fast, lightweight and secure data storage.

### Features Added
- User Requirements: User requirements have been fully documented (docs will be uploaded soon).
- System Design: Completed system design with class diagrams and ERDs.
- Menu Activity: Implemented with a fragment container for managing tasks, including adding and viewing tasks.
- Main Activity: Functions as the Login Activity, handling user authentication.
- Tasks Fragment: Added functionalities for adding and viewing tasks.
- Date and Time Stamp: Tasks now include a date and time stamp upon creation.
- User Registration: Implemented user registration to Firebase.
- Task Creation: Tasks can now be added and stored in Firestore (Create operation).
- Firebase Authentication: Integrated Firebase authentication for user login with email and password.

### Features to be Added
- Password Confirmation: Adding password confirmation during user registration.
- Task Viewing (Read): Enhance the task viewing feature with data retrieval from Firestore.
- Task Editing (Update): Implement the ability to edit tasks stored in Firestore.
- Task Deletion (Delete): Add functionality for deleting tasks from Firestore.
- Location Support: Optional feature to include location data with tasks.
- Phone Integration: Explore SQLite Dialog and ACTION_DIAL for phone call functionality.
- Camera Integration: Enable task-specific photo capture, storing images in the Firestore tasks collection.
- Testing: Plan for unit and automated testing, including concurrent tests.

### Documentation
For further guidance and detailed documentation, please refer to the following resources:

- Android Developers Documentation: Android Docs --> https://developer.android.com/guide/
- Firebase Documentation: Firebase Docs --> https://firebase.google.com/docs/
I appreciate your interest and involvement in this project as it continues to develop. Feedback is always welcome as I work towards improving and adding new features.

Note: Screenshots of the current interface will be included with this release.
## Screenshots
![login-screen 1.0.png](https://github.com/hieudku/TasksManagementApp/blob/master/login-screen%201.0.png)
![menu-screen 1.0.png](https://github.com/hieudku/TasksManagementApp/blob/master/menu-screen%201.0.png)
![](https://github.com/hieudku/TasksManagementApp/blob/master/menu-screen-frag-view%201.0.png)
![](https://github.com/hieudku/TasksManagementApp/blob/master/firestore-panel.png)
![](https://github.com/hieudku/TasksManagementApp/blob/master/firestore-query.png)
![](https://github.com/hieudku/TasksManagementApp/blob/master/auth-firebase.png)

