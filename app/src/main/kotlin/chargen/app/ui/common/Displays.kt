package chargen.app.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayFeature(feature: MutableState<FeatureData>, controller: MutableState<Boolean>) {
    if (controller.value) {
        AlertDialog(
            onDismissRequest = {
                controller.value = false
            },
            title = { Text("Feature ${feature.value.name}") },
            text = {
                //TODO: Fully display Feature Data contents
            },
            confirmButton = {
                Button(
                    onClick = {
                        controller.value = false
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                Button(
                    onClick = {
                        controller.value = false
                    }
                ) { Text("Dismiss") }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayProficiency(
    proficiency: MutableState<Proficiency>,
    controller: MutableState<Boolean>,
    onNameChanged: (name: String) -> Unit,
    onDescriptionChanged: (description: String) -> Unit
) {
    if (controller.value) {
        AlertDialog(
            onDismissRequest = {
                controller.value = false
            },
            title = { Text("Proficiency ${proficiency.value.name}") },
            text = {
                Column {
                    Row {
                        TextField(
                            value = proficiency.value.name,
                            modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                            label = { Text("Name") },
                            onValueChange = onNameChanged
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        TextField(
                            value = proficiency.value.description,
                            modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                            label = { Text("Description") },
                            onValueChange = onDescriptionChanged
                        )
                    }
                }
                Text(proficiency.value.description)
            },
            confirmButton = {
                Button(
                    onClick = {
                        controller.value = false
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                Button(
                    onClick = {
                        controller.value = false
                    }
                ) { Text("Dismiss") }
            }
        )
    }
}