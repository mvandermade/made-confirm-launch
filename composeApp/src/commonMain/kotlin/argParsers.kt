fun parseArgs(commandLineArguments: Array<String>): CmdArgs {
    // First the required ones
    var checkDrivePath: String? = null
    var checkFilePath: String? = null
    var program: String? = null
    var argument: String? = null
    var dryRun = false

    commandLineArguments.forEach { input ->
        if (input.startsWith("-checkDrivePath=")) {
            checkDrivePath = parseSplitArg(input)
        }
        if (input.startsWith("-checkFilePath=")) {
            checkFilePath = parseSplitArg(input)
        }
        if (input.startsWith("-argument=")) {
            argument = parseSplitArg(input)
        }
        if (input.startsWith("-program=")) {
            program = parseSplitArg(input)
        }
        if (input.startsWith("-dryRun")) {
            dryRun = true
        }
    }

    return CmdArgs(
        checkDrivePath = checkDrivePath ?: throw IllegalArgumentException("-checkDrivePath= cannot be missing"),
        checkFilePath = checkFilePath ?: throw IllegalArgumentException("-checkFilePath= cannot be missing"),
        dryRun = dryRun,
        argument = argument,
        program = program ?: throw IllegalArgumentException("-program= cannot be missing"),
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
