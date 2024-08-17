import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import provider.FileProviderJava

const val APP_NAME = "Made Confirm Launch"
const val APP_VERSION = "1.0.2"
const val APP_TITLE = "$APP_NAME $APP_VERSION"

fun main(args: Array<String>) {
    val cmdArgs = parseArgs(args)

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = APP_TITLE,
            state = rememberWindowState(width = 440.dp, height = 350.dp),
        ) {
            app(cmdArgs, FileProviderJava(), ::exitProcessWithReason)
        }
    }
}
