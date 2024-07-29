package screens

import AppState
import CmdArgs
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import appStateToIndicator
import rowPaddedModifier
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.exitProcess


@Composable
fun pageStartBackup(
    appState: AppState,
    cmdArgs: CmdArgs,
) {
    var topBarText = "Dryrun"
    var appText =
        """
        Programma zou zijn: ${cmdArgs.program} met argument: ${cmdArgs.argument}. Nu niks mee gedaan vanwege argument -dryRun
        Applicatie kan worden gesloten.
        """.trimIndent()

    if (!cmdArgs.dryRun) {
        topBarText = "Backupprogramma `${cmdArgs.argument}`wordt gestart..."
        appText =
            """
            Deze applicatie wacht totdat de backuptool klaar is.
            Sluiten van deze applicatie met (x) kan gevolgen hebben voor de backup...
            """.trimIndent()

        try {
            val process = if (cmdArgs.argument == null) {
                ProcessBuilder(cmdArgs.program).start()
            } else {
                ProcessBuilder(cmdArgs.program, cmdArgs.argument).start()
            }
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                println("Exitting code 0")
                exitProcess(0)
            } else {
                println("Exitting code -1")
                exitProcess(-1)
            }
        } catch (e: Exception) {
            topBarText = "Error tijdens starten backup applicatie :( Sluit over 5 sec..."
            appText = "Fout: ${e.message}"
            e.printStackTrace()
            Timer("StartBackupIndicator").schedule(5_000, 5_000) {
                exitProcess(1)
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
