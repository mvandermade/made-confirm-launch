package screens

import AppState
import CmdArgs
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import appStateToIndicator
import rowPaddedModifier

@Composable
fun page4WaitForAcknowledge(
    appState: AppState,
    cmdArgs: CmdArgs,
    requestNewState: (appState: AppState) -> Unit,
    countdownBackupButton: Int,
) {
    if (countdownBackupButton == 0) requestNewState(AppState.START_BACKUP)

    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = "Schijf gevonden!") },
                )
            }
            Row {
                Text(text = appStateToIndicator(appState))
            }
            Row(modifier = rowPaddedModifier) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        onClick = {
                            requestNewState(AppState.START_BACKUP)
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                backgroundColor = LightColors.Secondary.color,
                            ),
                    ) {
                        Text("Backup maken ($countdownBackupButton)")
                    }
                }
            }
            Row(modifier = rowPaddedModifier) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Gepland om uit te voeren: ${cmdArgs.exec}")
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
