package chargen.app.ui.window.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chargen.app.ui.common.*
import chargen.app.ui.window.RaceEdit
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun RaceEditContent(
    component: RaceEdit
) {
    val model by component.models.subscribeAsState()

    val proficiencyDisplay = remember { mutableStateOf(false) }
    val featureDisplay = remember { mutableStateOf(false) }
    var proficiencyToDisplay = remember { mutableStateOf(Proficiency.DEFAULT) }
    var featureToDisplay = remember { mutableStateOf(FeatureData.DEFAULT) }
    val featureListDisplay = remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Race Edit") },
            navigationIcon = {
                IconButton(onClick = component::onClosedClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            TextField(
                value = model.name,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Race Name") },
                onValueChange = component::onNameChanged
            )

            Spacer(modifier = Modifier.width(4.dp))

            TextField(
                value = model.namePlural,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Race Plural Name") },
                onValueChange = component::onNamePluralChanged
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            TextField(
                value = model.description,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Race Description") },
                onValueChange = component::onDescriptionChanged
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Box(Modifier.weight(1F)) {
                ProficiencyList(
                    items = model.proficiencies,
                    onItemClicked = { proficiency ->
                        proficiencyDisplay.value = true
                        proficiencyToDisplay.value = proficiency
                    },
                    onItemDeleteClicked = component::onRemoveProficiencyClicked
                )
            }

            ProficiencyInput(
                onItemAddClicked = component::onAddProficiencyClicked
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
            Box(Modifier.weight(1F)) {
                ItemList(
                    items = model.features,
                    onItemClicked = { featureId ->
                        featureDisplay.value = true
                        featureToDisplay.value = model.features.find { it.id == featureId }!!
                    },
                    onItemDeleteClicked = { featureId ->
                        component.onRemoveFeatureClicked(model.features.find { it.id == featureId }!!)
                    }
                )
            }

            ItemInput(
                onItemAddClicked = {
                    featureListDisplay.value = true
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))



        DisplayProficiency(proficiencyToDisplay, proficiencyDisplay, { name ->

        }, { description ->

        })
        DisplayFeature(featureToDisplay, featureDisplay)
        DisplayFeatureSelection(component::onAddFeatureClicked, featureListDisplay)
    }
}



@Composable
private fun DisplayFeatureSelection(
    onAddClicked: (feature: FeatureData) -> Unit,
    display: MutableState<Boolean>
) {
    Text("TODO: Display a list of all applicable Features")
}

@Composable
private fun StatMap(
    component: RaceEdit
) {

}