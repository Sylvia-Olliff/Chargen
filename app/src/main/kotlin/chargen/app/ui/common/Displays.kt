package chargen.app.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
fun DisplayProficiency(proficiency: MutableState<Proficiency>, controller: MutableState<Boolean>) {
    if (controller.value) {
        AlertDialog(
            onDismissRequest = {
                controller.value = false
            },
            title = { Text("Proficiency ${proficiency.value.name}") },
            text = { Text(proficiency.value.description) },
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