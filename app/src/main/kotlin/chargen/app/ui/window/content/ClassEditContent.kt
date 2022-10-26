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
import chargen.app.ui.window.ClassEdit
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@Composable
fun ClassEditContent(component: ClassEdit) {
    val model by component.models.subscribeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Edit Class") },
            navigationIcon = {
                IconButton(onClick = component::onCloseClicked) {
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
            label = { Text("Class Name") },
            onValueChange = component::onClassNameChanged
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.padding(8.dp)) {
            Text("is Caster?")

            Spacer(modifier = Modifier.width(4.dp))

            Checkbox(
                checked = model.isCaster,
                onCheckedChange = component::onIsCasterChange
            )
        }


    }
}