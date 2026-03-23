# QuizApp - Oblig 2

This application is a Flag Quiz app featuring persistent storage, structured MVVM architecture, and automated tests.

## Features
- **Persistent Storage**: Uses **Android Room** to save flag names and picture URIs.
- **Architecture**: Implements **Repository** and **ViewModel** patterns.
- **Rotation Handling**: Quiz state (current question and score) survives rotation thanks to ViewModel state management.
- **Content Provider**: Publishes the database to other apps with columns "name" and "URI".
- **Responsive UI**: Includes vertical scrolling for landscape mode and optimized image display for flags.

---

## Testing the Content Provider
The `GalleryContentProvider` is published under the authority `com.example.quizapp.provider`. 

**How to test using adb:**
1. Ensure the app is installed on your device/emulator.
2. Run the following command in your terminal:
```bash
adb shell content query --uri content://com.example.quizapp.provider/gallery_items

~/Library/Android/sdk/platform-tools/adb shell content query --uri content://com.example.quizapp.provider/gallery_items
```
*The second row is because I have a Mac and adb is not placed in the PATH. It is in the library folder.

**Expected Result**: The terminal will display a list of all flags currently in the database (e.g., China, Italy, Namibia) along with their internal image URIs.

---

## Test Case Documentation

### 1. Main Menu Navigation
*   **Description (Use Case)**: As a user on the Main Menu, I want to be able to access the Gallery and the Quiz. I tap the "Gallery" button to view my flags, and later I tap the "Quiz" button to start a new game.
*   **Expected Result**: The system should identify the button click and launch the correct Activity. The Gallery screen should display an "Add new entry" button, and the Quiz screen should display the current score.
*   **Implementation**: 
    *   Class: `com.example.quizapp.QuizAppTest`
    *   Methods: `testMainMenuToGallery()`, `testMainMenuToQuiz()`
*   **Result**: **Passed**

### 2. Quiz Scoring and Feedback (Correct & Wrong Answers)
*   **Description (Use Case)**: As a user playing the quiz, I want to see if my answer is correct and how it affects my score. I look at the image of a flag and tap one of the three country name options.
*   **Expected Results**:
    *   **Correct Answer**: Tapping the correct flag name increments both the score and the total count (e.g., "Score: 1 / 1"). The app displays "Correct!" feedback and a "Next" button.
    *   **Wrong Answer**: Tapping an incorrect flag name increments only the total count (e.g., "Score: 0 / 1"). The app displays "Wrong!" feedback and a "Next" button.
*   **Implementation**: 
    *   Class: `com.example.quizapp.QuizActivityTest`
    *   Methods: `testScoreUpdate()`, `testScoreWrong()`
*   **Result**: **Passed**

### 3. Adding a New Flag (Gallery Management)
*   **Description (Use Case)**: As a user, I want to expand my quiz by adding a new flag. I tap "Add new entry," select an image from my device, and type "Test Flag" in the name field.
*   **Expected Result**: After clicking "Add" in the dialog, the new entry "Test Flag" should be saved to the database and immediately appear as a new row in the Gallery list.
*   **Implementation**: 
    *   Class: `com.example.quizapp.GalleryActivityTest`
    *   Method: `testAddEntryWithIntentStubbing()`
    *   *Note: This test uses Intent Stubbing to simulate the user picking a file from the system storage.*
*   **Result**: **Passed**

---

## Troubleshooting & Configuration
- **Testing**: Espresso tests require animations to be disabled on the device (Window/Transition/Animator scale set to "Off" in Developer Options).
- **Dependencies**: Uses the latest stable Espresso 3.7.0 to ensure compatibility with Android 14+.
- **Database**: The app pre-loads 5 default flags (China, Italy, Namibia, Brazil, Bhutan) on the first launch.
