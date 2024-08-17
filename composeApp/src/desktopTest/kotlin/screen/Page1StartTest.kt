package screen

import CmdArgs
import androidx.compose.ui.test.junit4.createComposeRule
import app
import mocks.exitReasonMock
import org.junit.Rule
import provider.MockFileProvider
import waitUntilSubstringText
import waitUntilText
import kotlin.test.Test

class Page1StartTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Frontend shows initial page`() {
        cr.setContent {
            app(
                CmdArgs(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = false,
                    argument = "hello",
                    program = "freefilesync",
                ),
                arrayOf(""),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }

        cr.waitUntilText("Andere keer backuppen")
        cr.waitUntilText("Doorgaan >")
        cr.waitUntilSubstringText("Stap 1/5")
    }

    @Test
    fun `Frontend shows initial page dryRun`() {
        cr.setContent {
            app(
                CmdArgs(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = true,
                    program = "freefilesync",
                    argument = "freefilesync",
                ),
                arrayOf(""),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }

        cr.waitUntilText("Andere keer backuppen")
        cr.waitUntilText("Doorgaan >")
        cr.waitUntilSubstringText("Stap 1/5")
        cr.waitUntilSubstringText("De computer gaat alleen kijken (dryRun)")
    }
}
