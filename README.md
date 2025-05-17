# Fruit Vision

## Project Overview
Fruit Vision is an Android application designed for fruit recognition and management, utilizing TorchVision for image classification. The project integrates artificial intelligence to identify 12 types of fruits and provides a user-friendly interface for searching, tracking, and managing fruit-related information. It was developed to enhance educational and practical applications in fruit consumption and recognition.

## Repository Details
- **GitHub Link**: [https://github.com/HueyAnthonyDisward/Fruit-Vision](https://github.com/HueyAnthonyDisward/Fruit-Vision)
- **Languages/Tools**: Python, PyTorch, Android (Java/Kotlin), Flask, SQLite, CameraX, Material Design
- **Purpose**: Build a mobile tool for fruit recognition and user management using AI-powered image processing.

## Getting Started

### Prerequisites
- **For Android (`Fruit_Vision` folder)**:
  - Android Studio
  - Android SDK
  - CameraX library
  - SQLite
- **For Training (`Pytorch` folder)**:
  - Python 3.8 or higher
  - PyTorch
  - Flask
  - Required libraries: `torch`, `torchvision`, `numpy`

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/HueyAnthonyDisward/Fruit-Vision.git
   ```
2. **Navigate to the project directory**:
   ```bash
   cd Fruit-Vision
   ```
3. **Set up the Android application (`Fruit_Vision`)**:
   - Open the `Fruit_Vision` folder in Android Studio.
   - Sync the project with Gradle and install dependencies.
   - Ensure CameraX and Material Design libraries are configured.
4. **Set up the training environment (`Pytorch`)**:
   - Navigate to the `Pytorch` folder.
   - Install dependencies:
     ```bash
     pip install torch torchvision numpy flask
     ```
   - Place training images in the appropriate subdirectory within `Pytorch`.

### Usage
- **Android Application (`Fruit_Vision`)**:
  - Run the app in an Android emulator or on a physical device via Android Studio.
  - Log in with an email and password or register a new account.
  - Use the CameraX-integrated camera to capture fruit images or select from the gallery.
  - View recognition results and detailed fruit information, including history and search functionality.
- **Training Model (`Pytorch`)**:
  - Run the training script in the `Pytorch` folder to process images and update the model:
    ```bash
    python train.py
    ```
  - Deploy the Flask server for image prediction:
    ```bash
    python app.py
    ```
  - The Android app sends images to this server for recognition.

## Project Structure
- `.idea/`: IDE configuration files.
- `Fruit_Vision/`: Android application folder containing:
  - Source code for login, registration, image capture, fruit list, and user management.
  - SQLite database for storing user and search history data.
- `Pytorch/`: Training folder containing:
  - Python scripts for image preprocessing and model training with PyTorch.
  - Flask server for image prediction.
- `README.md`: This file.

## Methodology
- **Image Recognition**: Utilizes PyTorch and TorchVision to classify 12 fruit types (e.g., apple, banana, carrot, grape, mango, orange, pear).
- **Android Features**:
  - **Login/Registration**: Email/password authentication with SQLite storage.
  - **Image Capture**: CameraX for real-time photo capture or gallery selection.
  - **Fruit List**: CardView display with search and detailed views (e.g., botanical details).
  - **User Management**: Update personal info and view search history.
- **Server Integration**: Flask-based server processes images and returns recognition results.

## Results
- Successfully recognizes and classifies 12 fruit types with high accuracy.
- Provides a user-friendly interface with Material Design and efficient SQLite data management.
- Historical search data is preserved, enhancing user experience.

## Future Improvements
- Integrate 3D fruit models for enhanced visualization.
- Add cloud synchronization for user data and search history.
- Implement dark mode, multi-language support, and an introductory guide.


## Acknowledgments
- **Developer**: Nguyễn Trung Hiếu
- **Date**: April 17, 2025
