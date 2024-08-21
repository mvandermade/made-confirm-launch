import kotlin.test.Test
import kotlin.test.assertEquals

class ArgumentParsersTest {
    @Test
    fun `Parse correctly`() {
        // Gradle would need:
        // run --args="-checkDrivePath=/ -checkFilePath=/Users/mvandermade/myfile.txt -exec='nano myfile.txt'"
        // The single quotes are gone when the jdk parses it
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-program=nano",
                "-argument=myfile.txt",
            )

        val cmdArgs = fetchArguments(input)
        assertEquals("/", cmdArgs.checkDrivePath)
        assertEquals("/Users/mvandermade/myfile.txt", cmdArgs.checkFilePath)
        assertEquals(false, cmdArgs.dryRun)
        assertEquals("myfile.txt", cmdArgs.argument)
        assertEquals("nano", cmdArgs.program)
    }

    @Test
    fun `Parse correctly including repeated = marks`() {
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-program=nano",
                "-argument=myfile.txt?q=007",
            )

        val cmdArgs = fetchArguments(input)
        assertEquals("/", cmdArgs.checkDrivePath)
        assertEquals("/Users/mvandermade/myfile.txt", cmdArgs.checkFilePath)
        assertEquals(false, cmdArgs.dryRun)
        assertEquals("myfile.txt?q=007", cmdArgs.argument)
        assertEquals("nano", cmdArgs.program)
    }

    @Test
    fun `Missing checkDrivePath`() {
        val input =
            arrayOf(
                "-checkDrivePathDUMMY=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-program=nano",
                "-argument=myfile.txt?q=007",
            )

        val cmdArgs = fetchArguments(input)
        assertEquals(null, cmdArgs.checkDrivePath)
    }

    @Test
    fun `Missing checkFilePath`() {
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePathDUMMY=/Users/mvandermade/myfile.txt",
                "-program=nano",
                "-argument=myfile.txt?q=007",
            )
        val cmdArgs = fetchArguments(input)
        assertEquals(null, cmdArgs.checkFilePath)
    }

    @Test
    fun `Missing program`() {
        val input =
            arrayOf(
                "-checkDrivePath=/",
                "-checkFilePath=/Users/mvandermade/myfile.txt",
                "-programDUMMY=nano myfile.txt?q=007",
            )
        val cmdArgs = fetchArguments(input)
        assertEquals(null, cmdArgs.program)
    }

    @Test
    fun `Generate arguments preview null shows base`() {
        val result = generateArgumentsPreview(null, null, false, null, null)
        assertEquals("java -jar program.jar", result)
    }

    @Test
    fun `Generate arguments empty string shows base`() {
        val result = generateArgumentsPreview("", "", false, "", "")
        assertEquals("java -jar program.jar", result)
    }

    @Test
    fun `Generate arguments short string shows all`() {
        val result = generateArgumentsPreview("a", "b", false, "c", "d")
        assertEquals("java -jar program.jar -checkDrivePath=a -checkFilePath=b -program=c -argument=d", result)
    }
}
