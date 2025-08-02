import androidx.compose.runtime.*
import model.AppState
import provider.FileProvider
import screen.license
import screen.page2SearchRoot
import screen.page3SearchingFile
import screen.page4WaitForAcknowledge
import screen.pageStart
import screen.pageStartBackup

@Composable
fun app(
    appArguments: AppArguments,
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

    when (appState) {
        AppState.LICENSE -> license(::requestNewState)
        AppState.START -> pageStart(appState, appArguments, ::requestNewState, exitProcessWithReason)
        AppState.SEARCHING_ROOT -> {
            // TODO test this?
            if (appArguments.checkDrivePath != null && appArguments.checkDrivePath != "") {
                page2SearchRoot(
                    fileProvider,
                    appArguments.checkDrivePath,
                    appProgress,
                    ::requestNewState,
                    appState,
                    appArguments.description,
                )
            } else {
                requestNewState(AppState.SEARCHING_FILE)
            }
        }
        AppState.SEARCHING_FILE ->
            if (appArguments.checkFilePath != null && appArguments.checkFilePath != "") {
                page3SearchingFile(
                    fileProvider,
                    appArguments.checkDrivePath,
                    appArguments.checkFilePath,
                    appProgress,
                    ::requestNewState,
                    appState,
                    appArguments.description,
                )
            } else {
                requestNewState(AppState.WAIT_FOR_ACKNOWLEDGE)
            }
        AppState.WAIT_FOR_ACKNOWLEDGE ->
            page4WaitForAcknowledge(
                appState,
                appArguments,
                ::requestNewState,
            )
        AppState.START_BACKUP -> pageStartBackup(appState, appArguments, exitProcessWithReason)
    }
}
