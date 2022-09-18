package chargen.app.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import chargen.app.ui.window.content.CharacterEditContent
import chargen.app.ui.window.content.DataEditContent
import chargen.app.root.ChargenRoot.Child.*
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.*
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun ChargenRootContent(root: ChargenRoot, modifier: Modifier = Modifier) {
    val childStack by root.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Column(modifier = modifier) {
        Children(
            stack = childStack,
            modifier = Modifier.weight(weight = 1F),
            animation = tabAnimation()
        ) {
            when (val child = it.instance) {
                is CharacterEditChild -> CharacterEditContent(component = child.component, modifier = Modifier.fillMaxSize())
                is DataEditChild -> DataEditContent(component = child.component, modifier = Modifier.fillMaxSize())
            }
        }

        BottomNavigation(modifier = Modifier.fillMaxWidth()) {
            BottomNavigationItem(
                selected = activeComponent is CharacterEditChild,
                onClick = root::onEditCharacterClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Character"
                    )
                },
                label = { Text(text = "Edit Character") }
            )

            BottomNavigationItem(
                selected = activeComponent is DataEditChild,
                onClick = root::onEditDataClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Edit Data"
                    )
                },
                label = { Text("Edit Data") }
            )
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun tabAnimation(): StackAnimation<Any, ChargenRoot.Child> =
    stackAnimation { child, otherChild, direction ->
        val index = child.instance.index
        val otherIndex = otherChild.instance.index
        val anim = slide()
        if ((index > otherIndex) == direction.isEnter) anim else anim.flipSide()
    }

private val ChargenRoot.Child.index: Int
    get() =
        when (this) {
            is ChargenRoot.Child.CharacterEditChild -> 0
            is ChargenRoot.Child.DataEditChild -> 1
        }

@OptIn(ExperimentalDecomposeApi::class)
private fun StackAnimator.flipSide(): StackAnimator =
    StackAnimator { direction, onFinished, content ->
        invoke(
            direction = direction.flipSide(),
            onFinished = onFinished,
            content = content,
        )
    }

@Suppress("OPT_IN_USAGE")
private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }
