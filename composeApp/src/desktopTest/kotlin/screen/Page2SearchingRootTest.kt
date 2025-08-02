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

class Page2SearchingRootTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows the current drivepath mismatch with provider`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = false,
                    program = "freefilesync",
                    argument = "hello",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 2/5")
        cr.waitUntilText("descr")
        cr.waitUntilSubstringText("Zet schijf aan:")
        cr.waitUntilText("""Tot nu toe de volgende drives gevonden...:""")
        cr.waitUntilText("""A:\, /""")
    }
}
