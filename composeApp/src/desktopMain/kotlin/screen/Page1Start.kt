package screen

import AppArguments
import ExitReason
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import indicator.appStateToIndicator
import model.AppState
import theme.LightColors
import theme.rowPaddedModifier

@Composable
fun pageStart(
    appState: AppState,
    appArguments: AppArguments,
    requestNewState: (appState: AppState) -> Unit,
    exitProcessWithReason: (reason: ExitReason) -> Unit,
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
                        onClick = { exitProcessWithReason(ExitReason.EXIT_USER_ABORTED) },
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
                """.trimIndent()

            if (appArguments.dryRun) {
                text += "De computer gaat alleen kijken (dryRun)"
            }

            appArguments.description?.let { text += it }

            Row(modifier = rowPaddedModifier) {
                Text(fontFamily = FontFamily.Monospace, text = text)
            }
            Row {
                Button(
                    onClick = { requestNewState(AppState.LICENSE) },
                    colors =
                        ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.background,
                        ),
                ) {
                    Text("Licentie")
                }
            }
        }
    }
}
