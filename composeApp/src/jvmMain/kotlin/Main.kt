import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main(args: Array<String>) {
    val cmdArguments = fetchArguments(args)

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "made-confirm-launch ${System.getProperty("jpackage.app-version") ?: ""}",
            state = rememberWindowState(width = 500.dp, height = 450.dp),
        ) {
            init(cmdArguments)
        }
    }
}
