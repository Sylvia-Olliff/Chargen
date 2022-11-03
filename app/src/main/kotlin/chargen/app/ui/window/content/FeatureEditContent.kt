package chargen.app.ui.window.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chargen.app.ui.common.EnumSelectableList
import chargen.app.ui.window.FeatureEdit
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun FeatureEditContent(
    component: FeatureEdit
) {
    val model by component.models.subscribeAsState()
    var spellSlotsEnabled by remember { mutableStateOf((model.featureType == FeatureType.SPELL_SLOTS)) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Feature Edit") },
            navigationIcon = {
                IconButton(onClick = component::onCloseClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        Row(modifier = Modifier.padding(4.dp)) {
            TextField(
                value = model.name,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Feature Name") },
                onValueChange = component::onNameChanged
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = model.description,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Description") },
                onValueChange = component::onDescriptionChanged
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Row(modifier = Modifier.padding(4.dp)) {
            TextField(
                value = model.group,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Group Name") },
                onValueChange = component::onGroupNameChanged
            )

            Spacer(modifier = Modifier.width(4.dp))

            TextField(
                value = model.levelGained.toString(),
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Level Gained") },
                onValueChange = {
                    if (it.isNotEmpty()) {
                        try {
                            component.onLevelGainedChanged(it.toInt())
                        } catch (_: Exception) {}
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))

        EnumSelectableList(
            "Feature Type",
            selected = model.featureType
        ) {
            component.onFeatureTypeChanged(it)
            spellSlotsEnabled = it == FeatureType.SPELL_SLOTS
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Row(modifier = Modifier.padding(4.dp)) {
            TextField(
                value = model.value.toString(),
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Value") },
                onValueChange = {
                    if (it.isNotEmpty()) {
                        try {
                            component.onValueChanged(it.toInt())
                        } catch (_: Exception) {}
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Row(modifier = Modifier.padding(4.dp)) {
            EnumSelectableList(
                label = "Primary Stat",
                selected = model.stat ?: Stats.NONE,
                onClick = component::onStatChanged
            )

            Spacer(modifier = Modifier.width(4.dp))

            EnumSelectableList(
                label = "Source Stat",
                selected = model.sourceStat ?: Stats.NONE,
                onClick = component::onSourceStatChanged
            )
        }

        Divider()

        // TODO: Implement Spell Slots Editable Map
        Text("Implement Spell Slots Editable Map")

        Divider()

        // TODO: Implement Required Features List
        Text("Implement Required Features List")
    }
}
