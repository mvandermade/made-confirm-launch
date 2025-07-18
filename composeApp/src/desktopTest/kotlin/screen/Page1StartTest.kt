package screen

import AppArguments
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
                AppArguments(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = false,
                    argument = "hello",
                    program = "freefilesync",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }

        cr.waitUntilText("Andere keer backuppen")
        cr.waitUntilText("Doorgaan >")
        cr.waitUntilSubstringText("Stap 1/5")
        cr.waitUntilText("descr")
    }

    @Test
    fun `Frontend shows initial page dryRun`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = true,
                    program = "freefilesync",
                    argument = "freefilesync",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }

        cr.waitUntilText("Andere keer backuppen")
        cr.waitUntilText("Doorgaan >")
        cr.waitUntilSubstringText("Stap 1/5")
        cr.waitUntilSubstringText("De computer gaat alleen kijken (dryRun)")
    }

    @Test
    fun `Clicking the license button shows text containing`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """/mydrive""",
                    checkFilePath = "/media/usb/mydriveE.ffs_batch",
                    dryRun = false,
                    program = "freefilesync",
                    argument = "freefilesync",
                    description = "descr",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }

        cr.waitUntilText("Licentie")
        cr.onNodeWithText("Licentie").performClick()

        cr.waitUntilSubstringText("Martijn van der Made")
    }
}
