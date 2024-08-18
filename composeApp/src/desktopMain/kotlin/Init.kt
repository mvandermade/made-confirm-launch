import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import provider.FileProviderJava

@Composable
fun init(cmdArguments: CmdArguments) {
    var checkDrivePath by remember { mutableStateOf<String?>(null) }
    var checkFilePath by remember { mutableStateOf<String?>(null) }
    var dryRun by remember { mutableStateOf<Boolean>(false) }
    var program by remember { mutableStateOf<String?>(null) }
    var argument by remember { mutableStateOf<String?>(null) }

    if (cmdArguments.checkDrivePath != null) checkDrivePath = cmdArguments.checkDrivePath
    if (cmdArguments.checkFilePath != null) checkFilePath = cmdArguments.checkFilePath
    if (cmdArguments.program != null) program = cmdArguments.program

    argumentsOrNull(checkDrivePath, checkFilePath, dryRun, program, argument)?.let {
        app(it, FileProviderJava(), ::exitProcessWithReason)
    } ?: initScreen()
}

@Composable
fun initScreen() {
    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = "Vul de argumenten aan") },
                )
            }
            Row {
                Text(text = "Argument 1:")
            }
        }
    }
}

fun argumentsOrNull(
    checkDrivePath: String?,
    checkFilePath: String?,
    dryRun: Boolean,
    program: String?,
    argument: String?,
): AppArguments? {
    return AppArguments(
        checkDrivePath ?: return null,
        checkFilePath ?: return null,
        dryRun,
        program ?: return null,
        argument,
    )
}
