package screens

import AUTOMATIC_CONTINUE_SEARCHING_ROOT_MS
import AppState
import CmdArgs
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import appStateToIndicator
import rowPaddedModifier
import kotlin.system.exitProcess

@Composable
fun pageStart(
    appState: AppState,
    cmdArgs: CmdArgs,
    requestNewState: (appState: AppState) -> Unit,
    ttlMs: Long,
    counterWatcherMs: Long,
) {
    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = "Backup Maken?") },
                )
            }
            Row {
                Text(text = appStateToIndicator(appState))
            }
            Row {
                Column {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = { exitProcess(2) },
                        colors =
                            ButtonDefaults.buttonColors(
                                backgroundColor = LightColors.Secondary.color,
                            ),
                    ) {
                        Text("Andere keer backuppen")
                    }
                }
                Column {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            requestNewState(AppState.SEARCHING_ROOT)
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                backgroundColor = LightColors.Primary.color,
                            ),
                    ) {
                        Text(
                            "Doorgaan >",
                        )
                    }
                }
            }

            var text =
                """
                (Automatisch over: ${(AUTOMATIC_CONTINUE_SEARCHING_ROOT_MS - counterWatcherMs) / 1000} seconden)
                Uitvoeren: ${cmdArgs.exec}
                Op schijf: ${cmdArgs.checkDrivePath}
                Controle : ${cmdArgs.checkFilePath}                    
                """.trimIndent()

            if (cmdArgs.dryRun) {
                text +=
                    """
                    
                    De computer gaat alleen kijken (dryRun)
                    """.trimIndent()
            }

            Row(modifier = rowPaddedModifier) {
                Text(fontFamily = FontFamily.Monospace, text = text)
            }

            Row(modifier = rowPaddedModifier) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text =
                            """
                            Als je niks doet sluit de applicatie 
                            met "exit 1" over:
                            ${ttlMs / 1000}s
                            Deze backup popup zal via de Taakplanner regelmatig opnieuw worden getoond.
                            """.trimIndent(),
                    )
                }
            }
        }
    }
}
