import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import providers.FileProvider
import screens.page4WaitForAcknowledge
import screens.pageStart
import screens.pageStartBackup
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.schedule
import kotlin.system.exitProcess

val rowPaddedModifier =
    Modifier
        .padding(top = 10.dp, start = 15.dp, end = 15.dp)

val amountOfStates = 5

const val TASK_PERIOD_MS = 600L

// Needed to wait for app initialisation.
// FIXME try not to rely on cpu bound numbers
// If this value is too low you'll get state access warnings.
const val TASK_INITIAL_DELAY_MS = 1000L

// Seconds the application can be up
const val APP_TTL_MS = 300_000

/*
    Added some blocks of
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    because I didn't know how to refactor to function using the remembers values.
 */
fun appStateToIndicator(appState: AppState): String {
    return "Stap " +
        when (appState) {
            AppState.START -> "${AppState.START.ordinal + 1}/$amountOfStates"
            AppState.SEARCHING_ROOT -> "${AppState.SEARCHING_ROOT.ordinal + 1}/$amountOfStates"
            AppState.SEARCHING_FILE -> "${AppState.SEARCHING_FILE.ordinal + 1}/$amountOfStates"
            AppState.WAIT_FOR_ACKNOWLEDGE -> "${AppState.WAIT_FOR_ACKNOWLEDGE.ordinal + 1}/$amountOfStates"
            AppState.START_BACKUP -> "${AppState.START_BACKUP.ordinal + 1}/$amountOfStates"
        }
}

const val AUTOMATIC_CONTINUE_SEARCHING_ROOT_MS = 60_000

@Composable
fun app(
    cmdArgs: CmdArgs,
    pathsNeedsLookup: Array<String>,
    fileProvider: FileProvider,
) {
    var appState by remember { mutableStateOf(AppState.START) }

    // Time linit counters
    var counterWatcherMs by remember { mutableStateOf(0L) }
    val atomicCounter = AtomicLong(0)

    // Timers global
    var scanDrivePathTimer by remember { mutableStateOf<TimerTask?>(null) }
    val ttlTimer by remember {
        mutableStateOf<TimerTask>(
            Timer("TTL").schedule(TASK_INITIAL_DELAY_MS, 1_000) {
                val counterMs = atomicCounter.addAndGet(1_000)
                // Continue after 60s manually switching state
                if (counterMs > AUTOMATIC_CONTINUE_SEARCHING_ROOT_MS &&
                    appState == AppState.START
                ) {
                    appState = AppState.SEARCHING_ROOT
                }
                // Exit with failure
                if (counterMs > APP_TTL_MS) exitProcess(1)
                counterWatcherMs = counterMs
            },
        )
    }

    var countdownBackupButton by remember { mutableStateOf(10) }
    var buttonCountDownTimer by remember {
        mutableStateOf<TimerTask?>(
            Timer("CountDownTimer").schedule(TASK_INITIAL_DELAY_MS, TASK_PERIOD_MS) {
                if (appState != AppState.WAIT_FOR_ACKNOWLEDGE) return@schedule
                if (countdownBackupButton <= 0) return@schedule
                countdownBackupButton--
            },
        )
    }

    var fileDetectedTimer by remember { mutableStateOf<TimerTask?>(null) }

    // Indicators
    var scannerProgress by remember { mutableStateOf(90) }
    var appProgress by remember { mutableStateOf(0) }

    fun requestNewState(requestedNewAppState: AppState) {
        // This is probably accidental or race condition
        if (appState == requestedNewAppState) return

        // current appState cleanup things
        when (appState) {
            AppState.START -> {}
            AppState.SEARCHING_ROOT -> {
                scanDrivePathTimer?.cancel()
                scanDrivePathTimer = null
            }
            AppState.SEARCHING_FILE -> {
                fileDetectedTimer?.cancel()
                fileDetectedTimer = null
            }
            AppState.WAIT_FOR_ACKNOWLEDGE -> {
                fileDetectedTimer?.cancel()
                fileDetectedTimer = null
                buttonCountDownTimer?.cancel()
                buttonCountDownTimer = null
            }
            AppState.START_BACKUP -> {}
        }

        when (requestedNewAppState) {
            AppState.START -> {}
            AppState.SEARCHING_ROOT -> {}
            AppState.SEARCHING_FILE -> {}
            AppState.WAIT_FOR_ACKNOWLEDGE -> {}
            AppState.START_BACKUP -> ttlTimer.cancel()
        }
        // Switch to other state
        appState = requestedNewAppState
    }

    fun advanceCircularProgress() {
        if (scannerProgress > 100) {
            scannerProgress = 10
        } else {
            scannerProgress += 20
        }
    }

    fun advanceLinearProgress() {
        if (appProgress > 100) {
            appProgress = 55
        } else {
            appProgress += 20
        }
        // Also show some movement
        advanceCircularProgress()
    }
    // endof Indicators

    // Globals
    var rootWithSlashEndian by remember { mutableStateOf<String?>(null) }
    var drivesFoundIndicator by remember { mutableStateOf("...") }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (appState == AppState.START) {
        pageStart(appState, cmdArgs, ::requestNewState, APP_TTL_MS - counterWatcherMs, counterWatcherMs)
    } // AppState.START

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (appState == AppState.SEARCHING_ROOT) {
        var isDriveReady by remember { mutableStateOf(false) }
        scanDrivePathTimer =
            Timer("ScanDrivePath").schedule(TASK_INITIAL_DELAY_MS, TASK_PERIOD_MS) {
                if (isDriveReady) return@schedule

                // Detect slash-endian
                val roots = fileProvider.getRootPaths()
                val root: String? = getRootStartsWithOrNull(cmdArgs.checkDrivePath, roots)

                isDriveReady =
                    if (root == null) {
                        drivesFoundIndicator = roots.joinToString()
                        false
                    } else {
                        rootWithSlashEndian = root
                        drivesFoundIndicator = root
                        true
                    }
                advanceCircularProgress()
                if (isDriveReady) requestNewState(AppState.SEARCHING_FILE)
            }

        MaterialTheme {
            Column {
                Row {
                    TopAppBar(
                        title = { Text(text = "Zoeken naar schijf: ${cmdArgs.checkDrivePath}") },
                    )
                }
                Row {
                    Text(modifier = rowPaddedModifier, text = appStateToIndicator(appState))
                }
                Row {
                    Column {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = appProgress / 100.0f,
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
                                Als je niks doet sluit de applicatie
                                met "exit 1" over:
                                ${(APP_TTL_MS - counterWatcherMs) / 1000}s
                                Deze backup popup zal via de Taakplanner regelmatig opnieuw worden getoond.
                                """.trimIndent(),
                        )
                    }
                }
            }
        }
    } // endof SEARCHING_ROOT

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (appState == AppState.SEARCHING_FILE) {
        var isFileDetected by remember { mutableStateOf(false) }

        fileDetectedTimer =
            Timer("FileDetectedTimer").schedule(TASK_INITIAL_DELAY_MS, TASK_PERIOD_MS) {
                // Early return when drive is not ready FIXME how to suspend scheduled task?
                rootWithSlashEndian?.let { _ ->
                    if (isFileDetected) {
                        appProgress = 100
                        return@schedule
                    }
                    isFileDetected = fileProvider.doesFileExists(pathsNeedsLookup)

                    if (!isFileDetected) {
                        advanceLinearProgress()
                        return@schedule
                    }

                    requestNewState(AppState.WAIT_FOR_ACKNOWLEDGE)
                } ?: return@schedule
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
                            progress = appProgress / 100.0f,
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
                                Als je niks doet sluit de applicatie
                                met "exit 1" over:
                                ${(APP_TTL_MS - counterWatcherMs) / 1000}s
                                Deze backup popup zal via de Taakplanner regelmatig opnieuw worden getoond.
                                """.trimIndent(),
                        )
                    }
                }
            }
        }
    } // Endof SEARCHING_FILE

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (appState == AppState.WAIT_FOR_ACKNOWLEDGE) {
        page4WaitForAcknowledge(
            appState,
            cmdArgs,
            ::requestNewState,
            APP_TTL_MS - counterWatcherMs,
            countdownBackupButton,
        )
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    if (appState == AppState.START_BACKUP) {
        pageStartBackup(appState, cmdArgs)
    }
}
