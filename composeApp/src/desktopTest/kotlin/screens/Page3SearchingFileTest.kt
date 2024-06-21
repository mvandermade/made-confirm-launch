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

class Page3SearchingFileTest {
    @get:Rule
    val cr = createComposeRule()

    @Test
    fun `Shows the current filepath mismatch with provider`() {
        cr.setContent {
            app(
                CmdArgs(
                    checkDrivePath = """A:\""",
                    checkFilePath = """A:\MOCKED""",
                    dryRun = false,
                    exec = "freefilesync hello",
                ),
                arrayOf("""A:\DUMMYPATH""", """B:\ALSODUMMY"""),
                MockFileProvider(),
            )
        }
        cr.waitUntilText("Doorgaan >")
        cr.onNodeWithText("Doorgaan >").performClick()
        cr.waitUntilText("Stap 3/5")
        cr.waitUntilSubstringText("Aan het zoeken naar bestanden via meerdere combinaties...")
        cr.waitUntilSubstringText("Als je niks doet sluit de applicatie")
        cr.waitUntilSubstringText("""A:\DUMMYPATH, B:\ALSODUMMY""")
    }
}
