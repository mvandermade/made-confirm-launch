package screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import getSlashEndianness
import getTraversablePaths
import indicator.appStateToIndicator
import model.AppState
import provider.FileProvider
import theme.rowPaddedModifier
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun page3SearchingFile(
    fileProvider: FileProvider,
    checkDrivePath: String?,
    checkFilePath: String,
    appProgress: MutableState<Long>,
    requestNewState: (appState: AppState) -> Unit,
    appState: AppState,
    appDescription: String?
) {
    var isFileDetected by remember { mutableStateOf(false) }
    var scannerProgress by remember { mutableStateOf(90L) }
    val fileDetectedTimer = remember { mutableStateOf<TimerTask?>(null) }

    val pathsNeedsLookup =
        getTraversablePaths(
            drivePath = checkDrivePath,
            filePath = checkFilePath,
            slashEndianness = getSlashEndianness(checkDrivePath),
        )

    DisposableEffect("") {
        onDispose {
            fileDetectedTimer.value?.cancel()
            fileDetectedTimer.value = null
        }
    }

    fun advanceCircularProgress() {
        if (scannerProgress > 100) {
            scannerProgress = 10
        } else {
            scannerProgress += 20
        }
    }

    fun advanceLinearProgress() {
        if (appProgress.value > 100) {
            appProgress.value = 55
        } else {
            appProgress.value += 20
        }
        // Also show some movement
        advanceCircularProgress()
    }

    LaunchedEffect(true) {
        fileDetectedTimer.value =
            Timer("FileDetectedTimer").schedule(0, 1000L) {
                if (isFileDetected) {
                    appProgress.value = 100
                    return@schedule
                }
                isFileDetected = fileProvider.doesFileExists(pathsNeedsLookup)

                if (!isFileDetected) {
                    advanceLinearProgress()
                    return@schedule
                }

                requestNewState(AppState.WAIT_FOR_ACKNOWLEDGE)
            }
    }

    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = "Aan het zoeken naar bestanden via meerdere combinaties...") },
                )
            }
            Row {
                Text(modifier = rowPaddedModifier, text = appStateToIndicator(appState))
            }

            Row {
                Column {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = appProgress.value / 100.0f,
                    )
                }
            }
            Row(modifier = rowPaddedModifier) {
                Column {
                    CircularProgressIndicator(
                        progress = scannerProgress / 100.0f,
                    )
                }
            }
            Row(
                modifier = rowPaddedModifier,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = pathsNeedsLookup.joinToString())
                }
            }

            Row(modifier = rowPaddedModifier) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text =
                            """
                            Deze backup popup zal via de Taakplanner regelmatig opnieuw worden getoond.
                            """.trimIndent(),
                    )
                }
            }

            Row(modifier = rowPaddedModifier) {
                Text(fontFamily = FontFamily.Monospace, text = appDescription ?: "")
            }

        }
    }
}
