import androidx.compose.runtime.*
import model.AppState
import provider.FileProvider
import screen.page2SearchRoot
import screen.page3SearchingFile
import screen.page4WaitForAcknowledge
import screen.pageStart
import screen.pageStartBackup

@Composable
fun app(
    cmdArgs: CmdArgs,
    fileProvider: FileProvider,
    exitProcessWithReason: (reason: ExitReason) -> Unit,
) {
    var appState by remember { mutableStateOf(AppState.START) }

    // Indicators
    val appProgress = remember { mutableStateOf(0L) }

    fun requestNewState(requestedNewAppState: AppState) {
        // Switch to other state
        appState = requestedNewAppState
    }

    // Globals
    val rootWithSlashEndian = remember { mutableStateOf<String?>(null) }

    when (appState) {
        AppState.START -> pageStart(appState, cmdArgs, ::requestNewState, exitProcessWithReason)
        AppState.SEARCHING_ROOT ->
            page2SearchRoot(
                fileProvider,
                cmdArgs,
                rootWithSlashEndian,
                appProgress,
                ::requestNewState,
                appState,
            )
        AppState.SEARCHING_FILE ->
            page3SearchingFile(
                fileProvider,
                cmdArgs,
                rootWithSlashEndian,
                appProgress,
                ::requestNewState,
                appState,
            )
        AppState.WAIT_FOR_ACKNOWLEDGE ->
            page4WaitForAcknowledge(
                appState,
                cmdArgs,
                ::requestNewState,
            )
        AppState.START_BACKUP -> pageStartBackup(appState, cmdArgs, exitProcessWithReason)
    }
}
