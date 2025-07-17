fun getRootStartsWithOrNull(
    driveStartsWith: String,
    roots: List<String>,
): String? = roots.find { root -> root.startsWith(driveStartsWith) }

enum class SlashEndian(
    val slashToUse: String,
) {
    BACK_SLASH("""\"""),
    FORWARD_SLASH("""/"""),
}

fun getSlashEndianness(root: String?): SlashEndian {
    if (root == null) return SlashEndian.FORWARD_SLASH

    return if (root.contains("""\""")) {
        SlashEndian.BACK_SLASH
    } else {
        SlashEndian.FORWARD_SLASH
    }
}

fun getTraversablePaths(
    drivePath: String?,
    filePath: String,
    slashEndianness: SlashEndian,
): Array<String> =
    if (drivePath == null) {
        arrayOf(filePath)
    } else {
        arrayOf(
            // For unix systems
            filePath,
            // Windows correct
            """$drivePath$filePath""",
            // Windows only supplied letter
            """$drivePath:${slashEndianness.slashToUse}$filePath""",
            // Windows supplied for ex. C:
            """$drivePath${slashEndianness.slashToUse}$filePath""",
        )
    }
