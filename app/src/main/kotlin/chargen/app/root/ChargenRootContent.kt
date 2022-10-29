package chargen.app.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import chargen.app.root.ChargenRoot.Child.*
import chargen.app.ui.window.content.*
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
                is ClassEdit -> ClassEditContent(component = child.component)
                is ClassMain -> ClassMainContent(component = child.component)
                is CharacterEdit -> TODO()
                is FeatureEdit -> FeatureEditContent(component = child.component)
                is FeatureMain -> FeatureMainContent(component = child.component)
                is CharacterMain -> CharacterMainContent(component = child.component)
                is RaceEdit -> RaceEditContent(component = child.component)
                is RaceMain -> RaceMainContent(component = child.component)
                is SkillEdit -> SkillEditContent(component = child.component)
                is SkillMain -> SkillMainContent(component = child.component)
            }
        }

        BottomNavigation(modifier = Modifier.fillMaxWidth()) {
            BottomNavigationItem(
                selected = activeComponent is CharacterMain,
                onClick = root::onCharacterMainClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Edit Characters"
                    )
                },
                label = { Text(text = "Edit Characters") }
            )

            BottomNavigationItem(
                selected = activeComponent is ClassMain,
                onClick = root::onClassMainClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Edit Classes"
                    )
                },
                label = { Text("Edit Classes") }
            )

            BottomNavigationItem(
                selected = activeComponent is FeatureMain,
                onClick = root::onFeatureMainClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Edit Features"
                    )
                },
                label = { Text("Edit Features") }
            )

            BottomNavigationItem(
                selected = activeComponent is RaceMain,
                onClick = root::onRaceMainClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Edit Races"
                    )
                },
                label = { Text("Edit Races") }
            )

            BottomNavigationItem(
                selected = activeComponent is SkillMain,
                onClick = root::onSkillMainClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Edit Skills"
                    )
                },
                label = { Text("Edit Skills") }
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
            is ClassEdit -> 0
            is ClassMain -> 1
            is CharacterEdit -> 2
            is FeatureEdit -> 3
            is FeatureMain -> 4
            is CharacterMain -> 5
            is RaceEdit -> 6
            is RaceMain -> 7
            is SkillEdit -> 8
            is SkillMain -> 9
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
