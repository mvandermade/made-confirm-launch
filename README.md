# made-confirm-launch

## Introduction
The user is asked questions using popup style buttons. It is aimed at some filecopy program to check if the drive is physically attached. One might need such a method when the backup drive isn't permanently attached to the host. Some checks are performed on the inputted paths to make the interface simpler.
### Operating systems
- Windows
- MacOS
- Linux
(Follows the Kotlin Multiplatform compatibility)
### Requirements
- Java Runtime needs to be installed
![image](https://github.com/user-attachments/assets/320d487c-edd8-437b-8a65-10aff71b8885)
- (!) Right now the application is written in Dutch lanuage.

## How to get started
### Running jar
- Check if you have correct `java` version
- Easy download it here for example: https://learn.microsoft.com/nl-nl/java/openjdk/download
- `java -jar <the-name-of-the-donwload>.jar`

### Developers
- Select JDK17 (higher not supported right now)
- Run allTests task `./gradlew allTests` or to run: `./gradlew composeApp:run`
- Write some code!
# Note
- Ktlint is used for linting. CI fails when you forget it.
  - `./gradlew ktlintFormat`
- Customized editorconfig to match compose.
- Useful software:
  - Jetbrains IntelIJ
  - Jetbrains Fleet
  - Kotlin plugin for VSCode

### Running in development mode
- Running things:
  - `./gradlew composeApp:run`
  - Or use your IDE short run configs.
  - Use @Preview annotation in devTools folder.
  - Use the following arguments for gradle to supply --args
With spaces in argument
```text
desktopRun --args="-checkDrivePath=/ -checkFilePath=/Users/Shared/myfile.txt -program=freefilesync" -argument='/Users/Shared/BatchRun.ffs_batch'" -DmainClass=MainKt --quiet
```

## Some pointers
If this warning occurs:
```text
Could not determine the dependencies of task ':composeApp:compileKotlinDesktop'.
> WindowsRegistry is not supported on this operating system.
```
Check if JDK == 17

## Testing
Useful for testing:
```kotlin
cr.onRoot(useUnmergedTree = true).printToLog("TAG")
```

# Footnote
- Original KMP notes:
* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

Learn more about
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Samples](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-samples.html)
- [KMP libraries](https://github.com/terrakok/kmp-awesome)
