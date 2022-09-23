package chargen.app.ui.window.components

import chargen.app.ui.window.CharacterNew
import com.arkivanov.decompose.ComponentContext

class CharacterNewComponent constructor(
    componentContext: ComponentContext
): CharacterNew, ComponentContext by componentContext {
}