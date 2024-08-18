package screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import generateArgumentsPreview

@Composable
fun initScreen(
    checkDrivePath: MutableState<String?>,
    checkFilePath: MutableState<String?>,
    dryRun: MutableState<Boolean>,
    program: MutableState<String?>,
    argument: MutableState<String?>,
) {
    // Save the formdata until submit is pressed. This is because null will default to ""
    var formCheckDrivePath by remember { mutableStateOf(checkDrivePath.value) }
    var formCheckFilePath by remember { mutableStateOf(checkFilePath.value) }
    var formDryRun by remember { mutableStateOf(dryRun.value) }
    var formProgram by remember { mutableStateOf(program.value) }
    var formArgument: String? by remember { mutableStateOf(argument.value) }

    MaterialTheme {
        Column {
            Row {
                TopAppBar(
                    title = { Text(text = "Vul de argumenten aan") },
                )
            }
            Row {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth(0.5f),
                ) {
                    TextField(
                        label = { Text("-checkDrivePath") },
                        maxLines = 2,
                        value = formCheckDrivePath ?: "",
                        onValueChange = { formCheckDrivePath = it },
                    )
                }
                Column {
                    TextField(
                        label = { Text("-checkFilePath") },
                        maxLines = 2,
                        value = formCheckFilePath ?: "",
                        onValueChange = { formCheckFilePath = it },
                    )
                }
            }
            Row {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth(0.5f),
                ) {
                    TextField(
                        label = { Text("-program") },
                        maxLines = 2,
                        value = formProgram ?: "",
                        onValueChange = { formProgram = it },
                    )
                }
                Column {
                    TextField(
                        label = { Text("-argument") },
                        maxLines = 2,
                        value = formArgument ?: "",
                        onValueChange = { formArgument = it },
                    )
                }
            }
            Row {
                Column {
                    Button(
                        onClick = {
                            checkDrivePath.value = formCheckDrivePath
                            checkFilePath.value = formCheckFilePath
                            dryRun.value = formDryRun
                            program.value = formProgram
                            argument.value = formArgument
                        },
                    ) {
                        Text("Retry")
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxWidth(0.5f),
                ) {
                    Text("Dryrun")
                    Checkbox(
                        checked = formDryRun,
                        onCheckedChange = { formDryRun = it },
                    )
                }
            }
            Row {
                Text("Here a generated example to use for command line access:")
            }
            Row {
                Column(modifier = Modifier.fillMaxWidth(1f)) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value =
                            generateArgumentsPreview(
                                formCheckDrivePath,
                                formCheckFilePath,
                                formDryRun,
                                formProgram,
                                formArgument,
                            ),
                        onValueChange = { },
                    )
                }
            }
        }
    }
}
