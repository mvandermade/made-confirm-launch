import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PathFunctionsTest {
    @Test
    fun `expect null getRootStartsWithOrNull`() {
        getRootStartsWithOrNull(
            "F:",
            listOf("A:\\", "E:\\"),
        )
    }

    @Test
    fun `expect non-null getRootStartsWithOrNull`() {
        getRootStartsWithOrNull(
            "E:",
            listOf("A:\\", "E:\\"),
        )
    }

    @Test
    fun `expect backslash`() {
        val root = """C:\Windows\run32.dll"""
        assertEquals(SlashEndian.BACK_SLASH, getSlashEndianness(root))
    }

    @Test
    fun `expect default`() {
        val root = """C:"""
        assertEquals(SlashEndian.FORWARD_SLASH, getSlashEndianness(root))
    }

    @Test
    fun `expect forward`() {
        val root = """/mnt/media/md5sum.txt"""
        assertEquals(SlashEndian.FORWARD_SLASH, getSlashEndianness(root))
    }

    @Test
    fun `Expect forward slash null falls back`() {
        assertEquals(SlashEndian.FORWARD_SLASH, getSlashEndianness(null))
    }

    @Test
    fun `Expect traveable paths backslash style`() {
        val traversablePaths =
            getTraversablePaths(
                drivePath = "C:",
                filePath = """C:\Windows\run32.dll""",
                slashEndianness = SlashEndian.BACK_SLASH,
            )

        assertNotNull(
            traversablePaths.contains("""C:\Windows\run32.dll"""),
        )

        assertNotNull(
            traversablePaths.contains("""C:C:\Windows\run32.dll"""),
        )

        assertNotNull(
            traversablePaths.contains("""C:\C:\Windows\run32.dll"""),
        )
    }

    @Test
    fun `Expect unix style path with null drive`() {
        val traversablePaths =
            getTraversablePaths(
                drivePath = null,
                filePath = "/mnt/media/md5sum.txt",
                slashEndianness = SlashEndian.FORWARD_SLASH,
            )

        assertEquals(traversablePaths[0], "/mnt/media/md5sum.txt")
    }
}
