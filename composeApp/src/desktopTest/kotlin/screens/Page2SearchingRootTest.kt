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

class Page2SearchingRootTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows the current drivepath mismatch with provider`() {
        cr.setContent {
            app(
                CmdArgs(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = false,
                    exec = "freefilesync hello",
                ),
                arrayOf(""),
                MockFileProvider(),
            )
        }
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 2/5")
        cr.waitUntilSubstringText("Zoeken naar schijf")
        cr.waitUntilText("""Tot nu toe de volgende drives gevonden...:""")
        cr.waitUntilText("""A:\, /""")
        cr.waitUntilSubstringText("Als je niks doet sluit de applicatie")
    }
}
