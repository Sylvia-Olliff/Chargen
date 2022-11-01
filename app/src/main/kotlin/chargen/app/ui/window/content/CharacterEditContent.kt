package chargen.app.ui.window.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chargen.app.ui.window.CharacterEdit
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun CharacterEditContent(component: CharacterEdit) {
    val model by component.models.subscribeAsState()

    component.onLoad()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Edit Character") },
            navigationIcon = {
                IconButton(onClick = component::onCloseClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = model.playerName,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Player Name") },
                onValueChange = component::onPlayerNameChanged
            )

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
                value = model.characterName,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Character Name") },
                onValueChange = component::onCharacterNameChanged
            )

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
                value = model.campaignName,
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text("Campaign Name") },
                onValueChange = component::onCampaignNameChanged
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Row(modifier = Modifier.padding(8.dp)) {

        }
    }
}
