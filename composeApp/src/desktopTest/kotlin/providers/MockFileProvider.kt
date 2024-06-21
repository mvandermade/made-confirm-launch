package providers

class MockFileProvider : FileProvider {
    override fun getRootPaths(): List<String> {
        return listOf(
            """A:\""",
            """/""",
        )
    }

    override fun doesFileExists(pathsNeedsLookup: Array<String>): Boolean {
        return if (pathsNeedsLookup.isNotEmpty()) {
            return if (pathsNeedsLookup[0] == """A:\Agreed""") {
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}
