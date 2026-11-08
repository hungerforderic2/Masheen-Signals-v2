# Masheen Signals — Android Prototype

This is a fresh, corrected Android Studio project for the Masheen Signals HLP prototype.

## Build on GitHub using only your phone

1. Create a **new, empty GitHub repository** (recommended) or use a clean branch.
2. Extract this ZIP on your phone.
3. Upload the **contents** of the extracted `masheen_signals_android_v2` folder to the repository root. Keep the `app` folder intact.
4. Confirm the repository contains:
   - `.github/workflows/android.yml`
   - `settings.gradle.kts`
   - `build.gradle.kts`
   - `gradle.properties`
   - `app/build.gradle.kts`
   - `app/src/main/AndroidManifest.xml`
   - `app/src/main/java/com/hungyplusthamasheen/masheensignals/MainActivity.kt`
5. Open the repository's **Actions** tab. The workflow runs automatically on pushes to `main`, or choose **Build Masheen Signals → Run workflow**.
6. When the run succeeds, open it and download the `Masheen-Signals-APK` artifact. Extract that ZIP and install `app-debug.apk`.

## Build configuration

- Android Gradle Plugin 8.7.3
- Kotlin 2.0.21
- Gradle 8.9
- Java/Kotlin JVM target 17
- compileSdk 35; minSdk 26
- GitHub Actions uses the runner's preinstalled Android SDK; it deliberately does **not** invoke `android-actions/setup-android`, avoiding the earlier `sdkmanager` failure.

## Prototype scope

The app previews eight expressive signal categories, intensity levels 1–5, optional vibration and sound, and a color cue. It is a local prototype: no pairing, messaging, Bluetooth transport, or background signaling yet. Haptic strength and behavior vary by Android device. Signals are authored communication patterns, not claims that the machine literally experiences emotions.
