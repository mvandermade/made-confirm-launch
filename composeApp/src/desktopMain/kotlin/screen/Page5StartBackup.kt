package screen

import AppArguments
import ExitReason
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import indicator.appStateToIndicator
import model.AppState
import theme.rowPaddedModifier
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun pageStartBackup(
    appState: AppState,
    appArguments: AppArguments,
    exitProcessWithReason: (reason: ExitReason) -> Unit,
) {
    var topBarText = "Dryrun"
    var appText =
        """
        Programma zou zijn: ${appArguments.program} met argument: ${appArguments.argument}. Nu niks mee gedaan vanwege argument -dryRun
        Applicatie kan worden gesloten.
        """.trimIndent()

    if (!appArguments.dryRun) {
        topBarText = "Backupprogramma `${appArguments.argument}`wordt gestart..."
        appText =
            """
            Deze applicatie wacht totdat de backuptool klaar is.
            Sluiten van deze applicatie met (x) kan gevolgen hebben voor de backup...
            """.trimIndent()
        try {
            val process =
                if (appArguments.argument == null) {
                    ProcessBuilder(appArguments.program).start()
                } else {
                    ProcessBuilder(appArguments.program, appArguments.argument).start()
                }
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                exitProcessWithReason(ExitReason.EXIT_AFTER_CHILD_PROCESS_REPORTS_ZERO)
            } else {
                exitProcessWithReason(ExitReason.EXIT_AFTER_CHILD_PROCESS_REPORTS_NONZERO)
            }
        } catch (e: Exception) {
            topBarText = "Error tijdens starten backup applicatie :( Sluit over 5 sec..."
            appText = "Fout: ${e.message}"
            Timer("StartBackupIndicator").schedule(5_000, 5_000) {
                exitProcessWithReason(ExitReason.EXIT_AFTER_CHILD_PROCESS_LAUNCH_CAUSES_EXCEPTION)
            }
        }
    }

    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = topBarText) },
                )
            }
            Row {
                Text(modifier = rowPaddedModifier, text = appStateToIndicator(appState))
            }
            Text(
                modifier = rowPaddedModifier,
                text = appText,
            )
        }
    }
}
