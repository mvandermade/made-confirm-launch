package providers

import java.io.File

class FileProviderJava : FileProvider {
    override fun getRootPaths(): List<String> {
        return File.listRoots().map { file ->
            file.absolutePath
        }
    }

    override fun doesFileExists(pathsNeedsLookup: Array<String>): Boolean {
        for (path in pathsNeedsLookup) {
            val file = File(path)
            return file.exists()
        }
        return false
    }
}
