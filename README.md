# Conversa

ChatApp is an Android-based messaging application built with Kotlin and Firebase. It provides real-time chat functionality, allowing users to communicate seamlessly. The app leverages Firebase Authentication for secure login, Cloud Firestore for storing user details and chat histories, and Firebase Storage for handling user-uploaded profile pictures.

## Features

- **Real-Time Messaging:**  
  View recent chats similar to popular messaging apps, complete with profile pictures, last message previews, and timestamps.

- **User Search and Chat Initiation:**  
  Easily search for other users by name and start chatting directly from the search results.

- **Profile Management:**  
  Users can update their profile details, including uploading a new profile picture, changing their display name, and updating contact information.

- **Secure Authentication:**  
  Firebase Authentication ensures that only registered users can access the app.

- **Cloud-Based Data Storage:**  
  Chat histories and user details are securely stored in Firebase’s Cloud Firestore, while profile pictures are maintained in Firebase Storage.

## Built With

- **Kotlin:** Main programming language for Android development.
- **Firebase Authentication:** For secure user login and management.
- **Cloud Firestore:** For real-time database services to store chats and user details.
- **Firebase Storage:** To manage and store user profile images.

## Installation

1. **Clone the Repository:**
   ```
   git clone https://github.com/Kutubuddin-Rasel/Chat-App.git
   ```
2. **Open in Android Studio:**  
   Open the project in Android Studio to build and run the application.

3. **Configure Firebase:**  
   - Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
   - Add your Android app to the project.
   - Download the `google-services.json` file and place it in the app directory.
   - Enable Firebase Authentication, Cloud Firestore, and Firebase Storage in your Firebase project.

4. **Build and Run:**  
   Build the project in Android Studio and run the app on an emulator or a physical device.

## Usage

- **Chats:**  
  The home screen displays recent chats along with the contact's profile picture, last message, and message time.

- **People:**  
  Use the search bar to find users by name. Tap on a user to start a new chat.

- **Chat Screen:**  
  View your complete chat history with a selected contact and send messages in real time.

- **Profile:**  
  Update your personal details, upload a profile picture, and log out from your account.
  
## Contribution

Contributions are welcome! Please open an issue or submit a pull request for any improvements.
