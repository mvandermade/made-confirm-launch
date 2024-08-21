package screen

import AppArguments
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import app
import mocks.exitReasonMock
import org.junit.Rule
import org.junit.Test
import provider.MockFileProvider
import waitUntilSubstringText
import waitUntilText

class Page3SearchingFileTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows the current filepath mismatch with provider`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\MOCKED""",
                    dryRun = false,
                    program = "freefilesync",
                    argument = "hello",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 3/5")
        cr.waitUntilSubstringText("Aan het zoeken naar bestanden via meerdere combinaties...")
        cr.onRoot(useUnmergedTree = true).printToLog("TAG")

        cr.waitUntilSubstringText("""A:\MOCKED, A:\A:\MOCKED, A:\:\A:\MOCKED, A:\\A:\MOCKED""")
    }

    @Test
    fun `Shows the current filepath mismatch with null drive with provider`() {
        cr.setContent {
            app(
                AppArguments(
                    checkDrivePath = null,
                    checkFilePath = """A:\MOCKED""",
                    dryRun = false,
                    program = "freefilesync",
                    argument = "hello",
                ),
                MockFileProvider(),
                ::exitReasonMock,
            )
        }
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 3/5")
        cr.waitUntilSubstringText("Aan het zoeken naar bestanden via meerdere combinaties...")

        cr.waitUntilSubstringText("""A:\MOCKED""")
    }
}
