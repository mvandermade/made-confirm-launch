package screens

import CmdArgs
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app
import org.junit.Rule
import org.junit.Test
import providers.MockFileProvider
import waitUntilSubstringText
import waitUntilText

class Page5StartBackupTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows start backup dryrun`() {
        cr.setContent {
            app(
                CmdArgs(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\Agreed""",
                    dryRun = true,
                    exec = "freefilesync hello",
                ),
                arrayOf("""A:\Agreed"""),
                MockFileProvider(),
            )
        }
        // Wait until the program advances
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
        // A bit random timing here to click the number 7 takes a little less time than 3s.
        cr.waitUntilText("Backup maken (7)")
        cr.onNodeWithText("Backup maken (7)").performClick()
        cr.waitUntilText("Stap 5/5")
        cr.waitUntilSubstringText("Dryrun")
        cr.waitUntilSubstringText("Target zou zijn: freefilesync hello")
    }

    @Test
    fun `Shows start backup`() {
        cr.setContent {
            app(
                CmdArgs(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\Agreed""",
                    dryRun = false,
                    exec = "freefilesync hello",
                ),
                arrayOf("""A:\Agreed"""),
                MockFileProvider(),
            )
        }
        // Wait until the program advances
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
        // A bit random timing here to click the number 7 takes a little less time than 3s.
        cr.waitUntilText("Backup maken (7)")
        cr.onNodeWithText("Backup maken (7)").performClick()
        cr.waitUntilText("Stap 5/5")
        cr.waitUntilSubstringText("Deze applicatie wacht totdat de backuptool klaar is.")
        // Can't check much more here because the app closes.
    }
}
