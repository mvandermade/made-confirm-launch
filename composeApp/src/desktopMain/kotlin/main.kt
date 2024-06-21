import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import providers.FileProviderJava

const val APP_NAME = "made-cc (confirm copy)"
const val APP_VERSION = "1.0.1"
const val APP_TITLE = "$APP_NAME $APP_VERSION"

fun main(args: Array<String>) {
    args.forEach {
        println(it)
    }
    val cmdArgs = parseArgs(args)
    println(cmdArgs)

    val pathsNeedsLookup =
        getTraversablePaths(
            drivePath = cmdArgs.checkDrivePath,
            filePath = cmdArgs.checkFilePath,
            slashEndianness = getSlashEndianness(cmdArgs.checkDrivePath),
        )

    println("Scanning paths:")
    pathsNeedsLookup.forEach {
        println(it)
    }

    return application {
        Window(
            onCloseRequest = ::exitApplication,
            title = APP_TITLE,
            state = rememberWindowState(width = 400.dp, height = 500.dp),
        ) {
            app(cmdArgs, pathsNeedsLookup, FileProviderJava())
        }
    }
}
