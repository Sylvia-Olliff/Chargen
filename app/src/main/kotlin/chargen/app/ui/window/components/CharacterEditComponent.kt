package chargen.app.ui.window.components

import chargen.app.ui.window.CharacterEdit
import com.arkivanov.decompose.ComponentContext

class CharacterEditComponent constructor(
    componentContext: ComponentContext
): CharacterEdit, ComponentContext by componentContext {

}
