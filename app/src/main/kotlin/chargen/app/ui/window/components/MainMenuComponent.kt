package chargen.app.ui.window.components

import chargen.app.ui.window.MainMenu
import com.arkivanov.decompose.ComponentContext

class MainMenuComponent constructor(
    componentContext: ComponentContext
): MainMenu, ComponentContext by componentContext {

}
