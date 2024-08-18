fun fetchArguments(commandLineArguments: Array<String>): CmdArguments {
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

    // Inform command line users of missing arguments
    if (checkDrivePath == null) println("-checkDrivePath= cannot be missing")
    if (checkFilePath == null) println("-checkFilePath= cannot be missing")
    if (program == null) println("-program= cannot be missing")

    return CmdArguments(
        checkDrivePath = checkDrivePath,
        checkFilePath = checkFilePath,
        dryRun = dryRun,
        argument = argument,
        program = program,
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
