package chargen.app.root

import chargen.app.ui.window.components.CharacterEditComponent
import chargen.app.ui.window.components.DataEditComponent
import chargen.app.ui.window.components.MainMenuComponent
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
        initialStack = { listOf(Config.Main) },
        childFactory = ::child
    )

    override val childStack: Value<ChildStack<*, ChargenRoot.Child>> = stack

    private fun child(config: Config, componentContext: ComponentContext): ChargenRoot.Child =
        when (config) {
            is Config.Main -> ChargenRoot.Child.MainMenuChild(MainMenuComponent(componentContext))
            is Config.Characters -> ChargenRoot.Child.CharacterEditChild(CharacterEditComponent(componentContext))
            is Config.Data -> ChargenRoot.Child.DataEditChild(DataEditComponent(componentContext))
        }

    override fun onEditCharacterClicked() {
        navigation.bringToFront(Config.Characters)
    }

    override fun onEditDataClicked() {
        navigation.bringToFront(Config.Data)
    }

    override fun onMainMenuClicked() {
        navigation.bringToFront(Config.Main)
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Characters : Config
        @Parcelize
        object Data : Config
        @Parcelize
        object Main : Config
    }
}
