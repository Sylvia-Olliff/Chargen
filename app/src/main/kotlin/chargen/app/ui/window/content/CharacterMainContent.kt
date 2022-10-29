package chargen.app.ui.window.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import chargen.app.ui.common.ItemInput
import chargen.app.ui.common.ItemList
import chargen.app.ui.window.CharacterMain
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun CharacterMainContent(component: CharacterMain) {
    val models by component.models.subscribeAsState()

    Column {
        TopAppBar(title = { Text("Characters") })

        Box(Modifier.weight(1F)) {
            ItemList(
                models.items,
                onItemClicked = component::onCharacterSelected,
                onItemDeleteClicked = component::onCharacterDeleteClicked
            )
        }

        ItemInput(
            onItemAddClicked = component::onCharacterAddClicked
        )
    }
}