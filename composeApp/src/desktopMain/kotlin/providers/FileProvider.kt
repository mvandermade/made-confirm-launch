package providers

interface FileProvider {
    fun getRootPaths(): List<String>

    fun doesFileExists(pathsNeedsLookup: Array<String>): Boolean
}
