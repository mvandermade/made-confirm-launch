fun parseArgs(commandLineArguments: Array<String>): CmdArgs {
    // First the required ones
    var checkDrivePath: String? = null
    var checkFilePath: String? = null
    var exec: String? = null
    var dryRun = false

    commandLineArguments.forEach { cmdArg ->
        if (cmdArg.startsWith("-checkDrivePath=")) {
            checkDrivePath = parseSplitArg(cmdArg)
        }
        if (cmdArg.startsWith("-checkFilePath=")) {
            checkFilePath = parseSplitArg(cmdArg)
        }
        if (cmdArg.startsWith("-exec=")) {
            exec = parseSplitArg(cmdArg)
        }
        if (cmdArg.startsWith("-dryRun")) {
            dryRun = true
        }
    }

    return CmdArgs(
        checkDrivePath = checkDrivePath ?: throw IllegalArgumentException("-checkDrivePath= cannot be missing"),
        checkFilePath = checkFilePath ?: throw IllegalArgumentException("-checkFilePath= cannot be missing"),
        dryRun = dryRun,
        exec = exec ?: throw IllegalArgumentException("-exec= cannot be missing"),
    )
}

fun parseSplitArg(arg: String): String {
    val argumentInput = arg.split("=", limit = 2)
    return if (argumentInput.size == 2) {
        argumentInput[1]
    } else {
        throw IllegalArgumentException("Missing argument for $arg")
    }
}
