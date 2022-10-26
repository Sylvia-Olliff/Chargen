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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chargen.app.ui.common.VerticalScrollbar
import chargen.app.ui.common.rememberScrollbarAdapter
import chargen.app.ui.window.FeatureMain
import chargen.lib.character.data.dnd.features.FeatureData
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun FeatureMainContent(
    component: FeatureMain
) {
    val models by component.models.subscribeAsState()

    Column {
        TopAppBar(title = { Text("Features") })

        Box(Modifier.weight(1F)) {
            FeatureList(
                models.items,
                onItemClicked = component::onFeatureSelected,
                onFeatureDeleteClicked = component::onFeatureDeleteClicked
            )
        }

        FeatureInput(
            onFeatureAddClicked = component::onFeatureAddClicked
        )
    }
}

@Composable
private fun FeatureList(
    items: List<FeatureData>,
    onItemClicked: (id: Long) -> Unit,
    onFeatureDeleteClicked: (id: Long) -> Unit
) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(items) {
                Item(
                    item = it,
                    onItemClicked,
                    onFeatureDeleteClicked
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
private fun Item(
    item: FeatureData,
    onItemClicked: (id: Long) -> Unit,
    onFeatureDeleteClicked: (id: Long) -> Unit
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
            text = AnnotatedString(item.group),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = AnnotatedString(item.featureType.name),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onFeatureDeleteClicked(item.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun FeatureInput(
    onFeatureAddClicked: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onFeatureAddClicked) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}