package chargen.app.ui.window.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chargen.app.ui.common.ItemInput
import chargen.app.ui.common.ItemList
import chargen.app.ui.common.VerticalScrollbar
import chargen.app.ui.common.rememberScrollbarAdapter
import chargen.app.ui.window.SkillMain
import chargen.lib.character.data.dnd.skills.SkillData
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun SkillMainContent(
    component: SkillMain
) {
    val models by component.models.subscribeAsState()

    Column {
        TopAppBar(title = { Text("Skills") })

        Box(modifier = Modifier.weight(1F)) {
            SkillList(
                models.items,
                component::onSkillSelected,
                component::onSkillDeleteClicked
            )
        }

        ItemInput(component::onSkillAddClicked)
    }
}

@Composable
fun SkillList(
    items: List<SkillData>,
    onItemClicked: (id: Long) -> Unit,
    onSkillDeleted: (id: Long) -> Unit
) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(items) {
                Item(
                    item = it,
                    onItemClicked,
                    onSkillDeleted
                )

                Divider()
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = listState,
                itemCount = items.size,
                averageItemSize = 37.dp
            )
        )
    }
}

@Composable
fun Item(
    item: SkillData,
    onItemClicked: (id: Long) -> Unit,
    onSkillDeleted: (id: Long) -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = { onItemClicked(item.id) })) {
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = AnnotatedString(item.name),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = AnnotatedString(item.stat.name),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onSkillDeleted(item.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}