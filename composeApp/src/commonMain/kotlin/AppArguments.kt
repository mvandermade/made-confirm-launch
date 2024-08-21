// The app arguments has non-nullables
data class AppArguments(
    val checkDrivePath: String?,
    val checkFilePath: String?,
    val dryRun: Boolean,
    val program: String,
    val argument: String?,
)
