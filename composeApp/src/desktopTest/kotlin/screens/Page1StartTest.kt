package screens

import CmdArgs
import androidx.compose.ui.test.junit4.createComposeRule
import app
import org.junit.Rule
import providers.MockFileProvider
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
                    exec = "freefilesync hello",
                ),
                arrayOf(""),
                MockFileProvider(),
            )
        }

        cr.waitUntilText("Andere keer backuppen")
        cr.waitUntilText("Doorgaan >")
        cr.waitUntilSubstringText("/mydrive")
        cr.waitUntilSubstringText("/media/usb/mydriveE.ffs_batch")
        cr.waitUntilSubstringText("freefilesync hello")
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
                    exec = "freefilesync hello",
                ),
                arrayOf(""),
                MockFileProvider(),
            )
        }

        cr.waitUntilText("Andere keer backuppen")
        cr.waitUntilText("Doorgaan >")
        cr.waitUntilSubstringText("/mydrive")
        cr.waitUntilSubstringText("/media/usb/mydriveE.ffs_batch")
        cr.waitUntilSubstringText("freefilesync hello")
        cr.waitUntilSubstringText("Stap 1/5")
        cr.waitUntilSubstringText("De computer gaat alleen kijken (dryRun)")
    }
}
