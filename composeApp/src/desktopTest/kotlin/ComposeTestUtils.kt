import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithText
import kotlin.test.assertEquals

fun SemanticsNodeInteraction.assertEditableTextEquals(expectedEditableText: String) {
    val node = this
    var editableTextValue: String? = null
    for ((key, value) in node.fetchSemanticsNode().config) {
        if (key.name == "EditableText") {
            editableTextValue = value.toString()
        }
    }
    assertEquals(expectedEditableText, editableTextValue)
}

fun ComposeContentTestRule.waitUntilText(
    text: String,
    amount: Int = 1,
) {
    val cr = this
    cr.waitUntil(5_000) {
        cr.onAllNodesWithText(text).fetchSemanticsNodes().size == amount
    }
}

fun ComposeContentTestRule.waitUntilSubstringText(
    text: String,
    amount: Int = 1,
) {
    val cr = this
    cr.waitUntil(3_000) {
        cr.onAllNodesWithText(text, substring = true).fetchSemanticsNodes().size == amount
    }
}
