import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import provider.FileProviderJava
import screen.initScreen

@Composable
fun init(cmdArguments: CmdArguments) {
    val checkDrivePath = remember { mutableStateOf(cmdArguments.checkDrivePath) }
    val checkFilePath = remember { mutableStateOf(cmdArguments.checkFilePath) }
    val dryRun = remember { mutableStateOf(cmdArguments.dryRun) }
    val program = remember { mutableStateOf(cmdArguments.program) }
    val argument = remember { mutableStateOf(cmdArguments.argument) }
    val description = remember { mutableStateOf(cmdArguments.description) }

    argumentsOrNull(
        checkDrivePath.value,
        checkFilePath.value,
        dryRun.value,
        program.value,
        argument.value,
        description.value,
    )?.let {
        app(it, FileProviderJava(), ::exitProcessWithReason)
    } ?: initScreen(checkDrivePath, checkFilePath, dryRun, program, argument, description)
}
