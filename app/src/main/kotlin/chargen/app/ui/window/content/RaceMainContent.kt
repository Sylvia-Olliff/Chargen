package chargen.app.ui.window.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import chargen.app.ui.common.ItemInput
import chargen.app.ui.common.ItemList
import chargen.app.ui.window.RaceMain
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun RaceMainContent(
    component: RaceMain
) {
    val models by component.models.subscribeAsState()

    Column {
        TopAppBar(title = { Text("Races") })

        Box(modifier = Modifier.weight(1F)) {
            ItemList(
                models.items,
                component::onRaceSelected,
                component::onRaceDeleteClicked
            )
        }

        ItemInput(component::onRaceAddClicked)
    }
}
