NurOX Wallpapers - Android project source

This is a first working prototype project. It includes the dark glassmorphism home UI, wallpaper gallery, category filters, search, preview, tabs, and Android wallpaper apply bridge.

Important:
- This workspace does not have Android SDK/Gradle installed, so an APK could not be compiled here.
- The project uses remote sample wallpaper images; internet is required to load them.
- Login/signup, cloud accounts and a production wallpaper catalog are not connected yet. The current profile is a guest placeholder. Wallpaper saving and applying are implemented in the Android bridge.

Build APK:
1. Upload this project folder to a new GitHub repository.
2. Open the repository's Actions tab.
3. Select "Build NurOX APK" and tap "Run workflow".
4. When complete, open the run and download the artifact "NurOX-Wallpapers-debug-apk".
5. Extract it and install app-debug.apk on your Android phone.

You can also open this folder in Android Studio and build app > Build Bundle(s) / APK(s) > Build APK(s).
