package chargen.app.root

import chargen.app.ui.window.components.CharacterEditComponent
import chargen.app.ui.window.components.CharacterNewComponent
import chargen.app.ui.window.components.DataEditComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

class ChargenRootComponent constructor(
    componentContext: ComponentContext
): ChargenRoot, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(Config.CharacterNew) },
        childFactory = ::child
    )

    override val childStack: Value<ChildStack<*, ChargenRoot.Child>> = stack

    private fun child(config: Config, componentContext: ComponentContext): ChargenRoot.Child =
        when (config) {
            is Config.CharacterNew -> ChargenRoot.Child.CharacterNewChild(CharacterNewComponent(componentContext))
            is Config.CharacterEdit -> ChargenRoot.Child.CharacterEditChild(CharacterEditComponent(componentContext))
            is Config.Data -> ChargenRoot.Child.DataEditChild(DataEditComponent(componentContext))
        }

    override fun onNewCharacterClicked() {
        navigation.bringToFront(Config.CharacterNew)
    }

    override fun onEditCharacterClicked() {
        navigation.bringToFront(Config.CharacterEdit)
    }

    override fun onEditDataClicked() {
        navigation.bringToFront(Config.Data)
    }


    private sealed interface Config : Parcelable {
        @Parcelize
        object CharacterNew: Config
        @Parcelize
        object CharacterEdit : Config
        @Parcelize
        object Data : Config
    }
}
