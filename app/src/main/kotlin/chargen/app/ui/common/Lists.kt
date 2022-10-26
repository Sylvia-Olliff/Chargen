package chargen.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.templates.Proficiency

@Composable
fun <E : Enum<E>> EnumSelectableList(
    selected: E,
    onClick: (value: E) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
        Text(selected.name, modifier = Modifier.fillMaxWidth().clickable { expanded = true }.background(
            Color.Gray))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().background(
                Color.Red)
        ) {
            selected.javaClass.enumConstants.forEachIndexed { index, type ->
                DropdownMenuItem(onClick = {
                    onClick(type)
                    expanded = false
                }) {
                    Text(type.name)
                }
            }
        }
    }
}

@Composable
fun <T: DataEntity> ItemList(
    items: List<T>,
    onItemClicked: (id: Long) -> Unit,
    onItemDeleteClicked: (id: Long) -> Unit
) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(items) {
                Item(
                    item = it,
                    onItemClicked,
                    onItemDeleteClicked
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
private fun <T : DataEntity> Item(
    item: T,
    onItemClicked: (id: Long) -> Unit,
    onItemDeleteClicked: (id: Long) -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = { onItemClicked(item.id) })) {
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = AnnotatedString(item.name),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onItemDeleteClicked(item.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun ItemInput(
    onItemAddClicked: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onItemAddClicked) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ProficiencyList(
    items: List<Proficiency>,
    onItemClicked: (proficiency: Proficiency) -> Unit,
    onItemDeleteClicked: (proficiency: Proficiency) -> Unit
) {
    Box {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(items) {
                ProficiencyItem(
                    item = it,
                    onItemClicked,
                    onItemDeleteClicked
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
private fun ProficiencyItem(
    item: Proficiency,
    onItemClicked: (proficiency: Proficiency) -> Unit,
    onItemDeleteClicked: (proficiency: Proficiency) -> Unit
) {
    Row(modifier = Modifier.clickable(onClick = { onItemClicked(item) })) {
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = AnnotatedString(item.name),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = AnnotatedString(item.description),
            modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onItemDeleteClicked(item) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun ProficiencyInput(
    onItemAddClicked: (proficiency: Proficiency) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = { onItemAddClicked(Proficiency("Proficiency Name", "Proficiency Description")) }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}