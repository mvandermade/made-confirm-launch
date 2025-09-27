package screen

import AppArguments
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app
import mocks.exitReasonMock
import org.junit.Rule
import org.junit.Test
import provider.MockFileProvider
import waitUntilSubstringText
import waitUntilText

class Page5StartBackupTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows start backup, dryrun`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\Agreed""",
                    dryRun = true,
                    program = "freefilesync",
                    argument = "hello",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        // Wait until the program advances
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
        // A bit random timing here to click the number 7 takes a little less time than 3s.
        cr.waitUntilText("Backup maken (9)")
        cr.onNodeWithText("Backup maken (9)").performClick()
        cr.waitUntilText("Stap 5/5")
        cr.waitUntilSubstringText("Dryrun")
        cr.waitUntilSubstringText("Programma zou zijn: freefilesync met argument: hello")
    }

    @Test
    fun `Shows launch ok because bash is in most peoples path on windows install wsl to get it`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\Agreed""",
                    dryRun = false,
                    program = "bash",
                    argument = "-c",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        // Wait until the program advances
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
        // A bit random timing here to click the number 7 takes a little less time than 3s.
        cr.waitUntilText("Backup maken (9)")
        cr.onNodeWithText("Backup maken (9)").performClick()
        cr.waitUntilText("Stap 5/5")
        cr.waitUntilSubstringText("Deze applicatie wacht totdat de backuptool klaar is.")
        // Can't check much more here because the app closes.
    }

    @Test
    fun `Shows an error because on the test runner the program is not found`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\Agreed""",
                    dryRun = false,
                    program = "qwertyuiop",
                    argument = "hello",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        // Wait until the program advances
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
        // A bit random timing here to click the number 7 takes a little less time than 3s.
        cr.waitUntilText("Backup maken (9)")
        cr.onNodeWithText("Backup maken (9)").performClick()
        cr.waitUntilText("Stap 5/5")
        cr.waitUntilSubstringText("Error tijdens starten backup applicatie :( Sluit over 5 sec...")
        // Can't check much more here because the app closes.
    }
}
