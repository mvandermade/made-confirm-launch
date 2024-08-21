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

fun argumentsOrNull(
    checkDrivePath: String?,
    checkFilePath: String?,
    dryRun: Boolean,
    program: String?,
    argument: String?,
): AppArguments? {
    return AppArguments(
        checkDrivePath,
        checkFilePath,
        dryRun,
        program ?: return null,
        argument,
    )
}

fun generateArgumentsPreview(
    checkDrivePath: String?,
    checkFilePath: String?,
    dryRun: Boolean,
    program: String?,
    argument: String?,
): String {
    var preview = "java -jar program.jar"
    if (checkDrivePath != null && checkDrivePath != "") {
        preview += " -checkDrivePath=${surroundIfSpaces(checkDrivePath)}"
    }
    if (checkFilePath != null && checkFilePath != "") {
        preview += " -checkFilePath=${surroundIfSpaces(checkFilePath)}"
    }
    if (dryRun) {
        preview += " -dryRun=true"
    }
    if (program != null && program != "") {
        preview += " -program=${surroundIfSpaces(program)}"
    }
    if (argument != null && argument != "") {
        preview += " -argument=${surroundIfSpaces(argument)}"
    }

    return preview
}

// The command line arguments cannot contain raw spaces
fun surroundIfSpaces(str: String): String {
    return if (str.contains(" ")) {
        "'$str'"
    } else {
        str
    }
}
