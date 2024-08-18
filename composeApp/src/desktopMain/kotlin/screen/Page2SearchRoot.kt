package screen

import AppArguments
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import getRootStartsWithOrNull
import indicator.appStateToIndicator
import model.AppState
import provider.FileProvider
import theme.rowPaddedModifier
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun page2SearchRoot(
    fileProvider: FileProvider,
    appArguments: AppArguments,
    rootWithSlashEndian: MutableState<String?>,
    appProgress: MutableState<Long>,
    requestNewState: (appState: AppState) -> Unit,
    appState: AppState,
) {
    var scanDrivePathTimer by remember { mutableStateOf<TimerTask?>(null) }
    var drivesFoundIndicator by remember { mutableStateOf("...") }
    var scannerProgress by remember { mutableStateOf(0) }

    DisposableEffect("") {
        onDispose {
            scanDrivePathTimer?.cancel()
            scanDrivePathTimer = null
        }
    }

    fun advanceCircularProgress() {
        if (scannerProgress > 100) {
            scannerProgress = 10
        } else {
            scannerProgress += 20
        }
    }

    var isDriveReady by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        scanDrivePathTimer =
            Timer("ScanDrivePath").schedule(0, 1000) {
                if (isDriveReady) return@schedule

                // Detect slash-endian
                val roots = fileProvider.getRootPaths()
                val root: String? = getRootStartsWithOrNull(appArguments.checkDrivePath, roots)

                isDriveReady =
                    if (root == null) {
                        drivesFoundIndicator = roots.joinToString()
                        false
                    } else {
                        rootWithSlashEndian.value = root
                        drivesFoundIndicator = root
                        true
                    }
                advanceCircularProgress()
                if (isDriveReady) requestNewState(AppState.SEARCHING_FILE)
            }
    }

    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = "Zet schijf aan: ${appArguments.checkDrivePath}") },
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(text = "Tot nu toe de volgende drives gevonden...:")
                }
                Column(modifier = Modifier.fillMaxWidth(1f)) {
                    TextField(
                        value = drivesFoundIndicator,
                        onValueChange = {},
                        singleLine = true,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                    )
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
        }
    }
}
