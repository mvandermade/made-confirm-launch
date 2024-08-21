package screen

import AppArguments
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app
import mocks.exitReasonMock
import org.junit.Rule
import org.junit.Test
import provider.FileProviderJava
import provider.MockFileProvider
import waitUntilSubstringText
import waitUntilText

class Page4WaitForAcknowledgeTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows the current path mismatch with provider`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\Agreed""",
                    dryRun = false,
                    program = "freefilesync",
                    argument = "hello",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        // Wait until the program advances
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
        cr.waitUntilSubstringText("Backup maken")
        cr.waitUntilText("Gepland om uit te voeren: freefilesync met argument: hello")
    }

    @Test
    fun `Application does not use file provider with drive and file path null`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = null,
                    checkFilePath = null,
                    dryRun = false,
                    program = "freefilesync",
                    argument = "hello",
                ),
                FileProviderJava(),
                ::exitReasonMock,
            )
        }
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 4/5")
    }
}
