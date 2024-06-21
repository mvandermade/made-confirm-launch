import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ArgParsersTest {
    @Test
    fun `Parse correctly`() {
        // Gradle would need:
        // run --args="-checkDrivePath=/ -checkFilePath=/Users/mvandermade/myfile.txt -exec='nano myfile.txt'"
        // The single quotes are gone when the jdk parses it
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-exec=nano myfile.txt",
            )

        val cmdArgs = parseArgs(input)
        assertEquals("/", cmdArgs.checkDrivePath)
        assertEquals("/Users/mvandermade/myfile.txt", cmdArgs.checkFilePath)
        assertEquals(false, cmdArgs.dryRun)
        assertEquals("nano myfile.txt", cmdArgs.exec)
    }

    @Test
    fun `Parse correctly including repeated = marks`() {
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-exec=nano myfile.txt?q=007",
            )

        val cmdArgs = parseArgs(input)
        assertEquals("/", cmdArgs.checkDrivePath)
        assertEquals("/Users/mvandermade/myfile.txt", cmdArgs.checkFilePath)
        assertEquals(false, cmdArgs.dryRun)
        assertEquals("nano myfile.txt?q=007", cmdArgs.exec)
    }

    @Test
    fun `Missing checkDrivePath`() {
        val input =
            arrayOf(
                "-checkDrivePathDUMMY=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-exec=nano myfile.txt?q=007",
            )
        assertFailsWith<IllegalArgumentException> {
            parseArgs(input)
        }
    }

    @Test
    fun `Missing checkFilePath`() {
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePathDUMMY=/Users/mvandermade/myfile.txt",
                "-exec=nano myfile.txt?q=007",
            )
        assertFailsWith<IllegalArgumentException> {
            parseArgs(input)
        }
    }

    @Test
    fun `Missing exec`() {
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-execDUMMY=nano myfile.txt?q=007",
            )
        assertFailsWith<IllegalArgumentException> {
            parseArgs(input)
        }
    }
}
