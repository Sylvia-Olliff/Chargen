package chargen.app.ui.window.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import chargen.app.ui.window.ClassMain
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chargen.app.ui.common.ItemInput
import chargen.app.ui.common.ItemList
import chargen.app.ui.common.VerticalScrollbar
import chargen.app.ui.common.rememberScrollbarAdapter
import chargen.lib.character.data.dnd.classes.ClassData

@Composable
fun ClassMainContent(
    component: ClassMain
) {
    val models by component.models.subscribeAsState()

    Column {
        TopAppBar(title = { Text("Classes") })

        Box(Modifier.weight(1F)) {
            ItemList(
                models.items,
                onItemClicked = component::onClassSelected,
                onItemDeleteClicked = component::onClassDeletedClicked
            )
        }

        ItemInput(
            onItemAddClicked = component::onClassAddedClicked
        )
    }
}