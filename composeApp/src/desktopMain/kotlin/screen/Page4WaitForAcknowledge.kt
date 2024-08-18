package screen

import AppArguments
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import indicator.appStateToIndicator
import model.AppState
import theme.LightColors
import theme.rowPaddedModifier
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun page4WaitForAcknowledge(
    appState: AppState,
    appArguments: AppArguments,
    requestNewState: (appState: AppState) -> Unit,
) {
    var countdownBackupButton by remember { mutableStateOf(10) }
    var buttonCountDownTimer by remember { mutableStateOf<TimerTask?>(null) }
    LaunchedEffect(true) {
        buttonCountDownTimer =
            Timer("CountDownTimer").schedule(0, 1000) {
                if (appState != AppState.WAIT_FOR_ACKNOWLEDGE) return@schedule
                if (countdownBackupButton <= 0) return@schedule
                countdownBackupButton--
            }
    }

    DisposableEffect("") {
        onDispose {
            buttonCountDownTimer?.cancel()
            buttonCountDownTimer = null
        }
    }

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
                    Text(
                        text =
                            "Gepland om uit te voeren: ${appArguments.program} " +
                                "met argument: ${appArguments.argument}",
                    )
                }
            }
        }
    }
}
