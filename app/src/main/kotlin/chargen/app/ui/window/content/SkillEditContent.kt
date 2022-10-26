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
import chargen.app.ui.common.EnumSelectableList
import chargen.app.ui.window.SkillEdit
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun SkillEditContent(
    component: SkillEdit
) {
    val model by component.models.subscribeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Skill Edit") },
            navigationIcon = {
                IconButton(onClick = component::onClosedClicked) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )

        TextField(
            value = model.name,
            modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
            label = { Text("Skill Name") },
            onValueChange = component::onNameChanged
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = model.description,
            modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
            label = { Text("Skill Description") },
            onValueChange = component::onDescriptionChanged
        )

        Spacer(modifier = Modifier.height(8.dp))

        EnumSelectableList(
            model.stat,
            component::onStatChanged
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Can use untrained?")
        Checkbox(
            checked = model.untrained,
            onCheckedChange = component::onUntrainedChanged
        )
    }
}