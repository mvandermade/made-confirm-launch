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
    requestNewState: (appState: AppState) -> Unit
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
                            Deze backup popup zal via de Taakplanner regelmatig opnieuw worden getoond.
                            """.trimIndent(),
                    )
                }
            }
        }
    }
}
