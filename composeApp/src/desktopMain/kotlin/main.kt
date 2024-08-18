import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

const val APP_TITLE = "Made Confirm Launch"

fun main(args: Array<String>) {
    val cmdArguments = fetchArguments(args)

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = APP_TITLE,
            state = rememberWindowState(width = 500.dp, height = 450.dp),
        ) {
            init(cmdArguments)
        }
    }
}
