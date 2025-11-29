# made-confirm-launch

## Running
1. Downloads avaiable under releases to the right from here, or build your own.

Windows:
1. Install using the installer
2. Setup Task Scheduler (via start menu)
3. Create a new task (editting looks like this)
<img width="566" height="624" alt="image" src="https://github.com/user-attachments/assets/e87c55a3-c408-45de-b9ac-9016a06ae5d7" />
Example:
- Program/script: "C:\Program files\made-confirm-launch\made-confirm-launch.exe"
- Parameters: -checkDrivePath=G:\ -program="C:\Program Files\FreeFileSync\FreeFileSync" -argument=G:\SyncSettings.ffs_batch -description="Connect the blue colored disk to begin!"

... done!

## Introduction
The user is asked questions using popup style buttons. The goal is to be able to let the user attach their correct backup drive before starting an external backup util.

1. When agument program is missing you get presented a helper:![image](docs/screenshot00.png)
2. The regular screen when fed correct arguments ![image](docs/screenshot01.png)
3. Checks if drive is present ![image](docs/screenshot02.png)
4. Checks if file is present ![image](docs/screenshot03.png)
5. Every check OK countdown in place because some disks are slow... ![image](docs/screenshot04.png)

### Operating systems
Follows the Kotlin Multiplatform compatibility: build your own executable using Gradle for now.

### Developers
- Select JDK17 (higher not supported right now)
- Run allTests task `./gradlew allTests` or to run: `./gradlew composeApp:run`
- Write some code!
- Ktlint is used for linting. CI fails when you forget it.
  - `./gradlew ktlintFormat`
- Customized editorconfig to match compose.
- Useful software:
  - Jetbrains IntelIJ
  - Jetbrains Fleet
  - Kotlin plugin for VSCode
 
### Testing
Useful for testing:
```kotlin
cr.onRoot(useUnmergedTree = true).printToLog("TAG")
```

### Dependency updates
```shell
gradle dependencyUpdates
```

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
